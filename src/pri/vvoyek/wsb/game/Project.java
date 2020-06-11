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
        public final static int MAX_DAYS = 28;

        public WorkItem(Technology t, int days) {
            this.t = t;
            this.days = days;
            this.remainingDays = days;
        }

        public String toString() {
            return t + "_" + days + "d";
        }

        public boolean isDone() {
            return remainingDays == 0;
        }
    }

    public final Client owner;
    public final String name;
    public final Date deadline;
    public final double price;
    public final double penalty;
    public int paymentDelay;
    public double downPayment;
    private List<WorkItem> workItems;

    public Project(Client owner, String name, Date deadline, double price, double penalty, int paymentDelay, double downPayment) {
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
        for (WorkItem wi : workItems)
            sb.append(" " + wi);
        return sb.toString();
    }

    public boolean isDone() {
        for (WorkItem wi : workItems)
            if (!wi.isDone())
                return false;
        return true;
    }

    public static Project generateNewProject(Client client) {

        Random r = new Random();
        String n = "Project";
        Project p = new Project(client,
                n,
                new Date(2020, 6,18),
                100.0,
                20.0,
                7,
                10.0);

        int count = 1 + r.nextInt(Technology.values().length);
        for (int i = 0; i < count; i++) {
            int days = 1 + r.nextInt(WorkItem.MAX_DAYS);
            Technology t = Technology.values()[r.nextInt(Technology.values().length)];
            WorkItem wi = new WorkItem(t, days);
            p.workItems.add(wi);
        }
        return  p;
    }
}
