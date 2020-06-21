package pri.vvoyek.wsb.game;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Project {

    public static class WorkItem {
        public final Technology t;
        public final int days;
        public int remainingDays;

        public WorkItem(Technology t, int days) {
            this.t = t;
            this.days = days;
            this.remainingDays = days;
        }

        public String toString() {
            return t + "_" + days + "d" + remainingDays;
        }

        public boolean isDone() {
            return remainingDays == 0;
        }

        public void work() { remainingDays --; }
    }

    public final Client owner;
    public final String name;
    public final int margin;
    public final double penalty;
    public int paymentDelay;
    public double payment = 0.0;
    private List<WorkItem> workItems;
    public int bugs = 0;
    public int debugDays = 0;
    public Company contractor;
    public LocalDate deadline;
    public LocalDate deliveryDate;
    public LocalDate paymentDate;

    private Project(Client owner, String name, int margin, double penalty, int paymentDelay) {
        this.owner = owner;
        this.name = name;
        this.margin = margin;
        this.penalty = penalty;
        this.paymentDelay = paymentDelay;
        this.workItems = new ArrayList<>();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(name);
        for (WorkItem wi : workItems) {
            sb.append(" ");
            sb.append(wi);
        }
        if (hasBugs()) {
            sb.append(" bugs ");
            sb.append(bugs);
            sb.append("/");
            sb.append(debugDays);
        }
        return sb.toString();
    }

    public String toLongString() {
        return toString() + " wartość " + getPrice() + " (" + getMargin() + "%) płatność " + paymentDelay + " dni termin oddania " + daysForDelivery() + " dni";
    }

    public boolean isSimple() {
        return workItems.size() == 1;
    }

    public boolean isMedium() {
        return !isSimple() && !isComplex();
    }

    public boolean isComplex() {
        return workItems.size() > 2 && getWorkDays() > Settings.COMPLEX_WORK_ITEM_DAYS;
    }

    public int getWorkDays() {
        int workdays = 0;
        for (WorkItem wi : workItems) {
            workdays += wi.days;
        }
        return workdays;
    }

    public int daysForDelivery() {
        if (isComplex()) {
            return getWorkDays() * 2;
        } else if (isMedium()) {
            return getWorkDays() * 3 / 2;
        } else {
            return getWorkDays();
        }
    }

    public int getMargin() {
        return margin * workItems.size();
    }

    public double getPrice() {
        return Math.round(getWorkDays() * Settings.PROJECT_FAIR_COST * (100.0 + getMargin()) / 100);
    }

    public double getDownPayment() {
        if (isComplex()) {
            return getPrice() * Settings.PROJECT_DOWN_PAYMENT;
        }
        return 0.0;
    }

    public boolean isDone() {
        for (WorkItem wi : workItems)
            if (!wi.isDone())
                return false;
        return true;
    }

    public boolean hasBugs() {
        return bugs > debugDays;
    }

    public void debug() {
        if (hasBugs()) {
            debugDays++;
        }
    }

    public boolean hasTechnology(Technology t) {
        for (WorkItem wi : workItems)
            if (wi.t.equals(t))
                return true;
        return false;
    }

    public boolean doTheJob(Programmer programmer, boolean tested) {
        for (WorkItem wi : workItems) {
            if (wi.isDone())
                continue;
            if (programmer.hasSkill(wi.t)) {
                if (programmer.onTime())
                    wi.work();
                if (!tested && !programmer.bugFree()) {
                    this.bugs ++;
                }
                return true;
            }
        }
        return false;
    }

    public boolean wasFullyPaid() {
        return payment >= getPrice();
    }

    private static String[] codenames = new String[] {
            "Acrux", "Bosona", "Cursa", "Diya", "Emiw", "Franz",
            "Ginan", "Heze", "Itonda", "Jabbah", "Kang", "Lema",
            // ...
    };
    private static int[] codenameVersions = new int[codenames.length];

    public static Project generateNewProject(Client client) {
        int margin = Game.nextInt(Settings.PROJECT_MAX_MARGIN);
        int paymentDelay = Game.nextInt(Settings.PROJECT_MAX_PAYMENT_DELAY);
        int codenameIndex = Game.nextInt(codenames.length);
        String n = codenames[codenameIndex] +  ++codenameVersions[codenameIndex];
        Project p = new Project(client,
                n,
                margin,
                20.0,
                paymentDelay);

        Technology[] technologies = Technology.getRandomTechnologies();
        for (Technology t : technologies) {
            int days = 1 + Game.nextInt(Settings.WORK_ITEM_MAX_DAYS);
            WorkItem wi = new WorkItem(t, days);
            p.workItems.add(wi);
        }

        System.out.println("nowy projekt " + p.toLongString());
        return  p;
    }
}
