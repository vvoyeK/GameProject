package pri.vvoyek.wsb.game;

public class Salesman extends Employee {
    private int daysAtWork = 0;

    public Salesman (String name) {
        super(name);
    }

    public String toString() {
        return name + " SPRZEDAWCA";
    }

    public void searchForProjects() {
        if (++daysAtWork % Settings.DAYS_TO_NEW_PROJECT == 0) {
            Market.getInstance().addNewProject(this);
        }
    }
}
