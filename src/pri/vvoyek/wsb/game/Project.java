package pri.vvoyek.wsb.game;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
    public final Date deadline;
    public final double price;
    public final double penalty;
    public int paymentDelay;
    public double downPayment;
    private List<WorkItem> workItems;
    public int bugs = 0;
    public int debugDays = 0;

    private Project(Client owner, String name, Date deadline, double price, double penalty, int paymentDelay, double downPayment) {
        this.owner = owner;
        this.name = name;
        this.deadline = deadline;
        this.price = price;
        this.penalty = penalty;
        this.paymentDelay = paymentDelay;
        this.downPayment = downPayment;
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

    public boolean doTheJob(Programmer programmer) {
        for (WorkItem wi : workItems) {
            if (wi.isDone())
                continue;
            if (programmer.hasSkill(wi.t)) {
                wi.work();
                if (!programmer.bugFree()) {
                    this.bugs ++;
                }
                return true;
            }
        }
        return false;
    }

    private static String[] codenames = new String[] {
            "Acrux", "Bosona", "Cursa", "Diya", "Emiw", "Franz",
            "Ginan", "Heze", "Itonda", "Jabbah", "Kang", "Lema",
            // ...
    };
    private static int[] codenameVersions = new int[codenames.length];

    public static Project generateNewProject(Client client) {

        int codenameIndex = Game.nextInt(codenames.length);
        String n = codenames[codenameIndex] +  ++codenameVersions[codenameIndex];
        Project p = new Project(client,
                n,
                new Date(2020, 6,18),
                100.0,
                20.0,
                7,
                10.0);

        int count = 1 + Game.nextInt(Technology.values().length);
        for (int i = 0; i < count; i++) {
            int days = 1 + Game.nextInt(Settings.WORK_ITEM_MAX_DAYS);
            Technology t = Technology.values()[Game.nextInt(Technology.values().length)];
            if (p.hasTechnology(t))
                continue;
            WorkItem wi = new WorkItem(t, days);
            p.workItems.add(wi);
        }

        System.out.println("nowy projekt " + p);
        return  p;
    }
}
