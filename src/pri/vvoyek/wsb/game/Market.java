package pri.vvoyek.wsb.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Market {

    private static Market instance = new Market();

    public static Market getInstance() {
        return instance;
    }

    public List<Client> clients = new ArrayList<>();
    public List<Project> projects = new ArrayList<>();
    public List<Employee> employees = new ArrayList<>();
    private int counter = 0;

    private Market() {
        for (int i = 0; i < Settings.INITIAL_PROJECT_COUNT; i++)
            addNewProject();
        for (int i = 0; i < Settings.INITIAL_FREE_EMPLOYEES; i++) {
            addNewEmployee();
        }
    }

    private Client getRandomClient() {
        Client c;

        int i = new Random().nextInt(clients.size() + 1);
        if (i < clients.size()) {
            c = clients.get(i);
        } else {
            c = Client.getRandomClient();
            clients.add(c);
        }
        return c;
    }

    private void addNewProject() {
        Client cl = getRandomClient();
        Project p = Project.generateNewProject(cl);
        projects.add(p);
    }

    public void showAvailableProjects() {
        for (Project p : projects)
            System.out.println(p);
    }

    public void searchForNewProject() {
        if (++counter % Settings.TRIES_TO_NEW_PROJECT == 0) {
            addNewProject();
        }
    }

    public Project findProject(String projectName) {
        for (Project p : projects) {
            if (p.name.equals(projectName)) {
                return p;
            }
        }
        return null;
    }

    public void addNewEmployee() {
        Employee e = null;
        switch (Game.nextInt(3)) {
            case 0:
                e = Programmer.getNewProgrammer(
                        Game.nextInt(Settings.PROGRAMMER_MAX_BUG_RATE),
                        Game.nextInt(Settings.PROGRAMMER_MAX_DELAY_RATE));
                break;
            case 1:
                e = new Tester(Employee.getNextName());
                break;
            case 2:
                e = new Salesman(Employee.getNextName());
                break;
            default:
                return;
        }
        employees.add(e);
        System.out.println("Nowy pracownik " + e);
    }

    public void showAvailableEmployees() {
        for (Employee e : employees)
            System.out.println(e);
    }

    public Employee findEmployee(String name) {
        for (Employee e : employees) {
            if (e.name.equals(name)) {
                return e;
            }
        }
        return null;
    }
}
