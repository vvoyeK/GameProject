package pri.vvoyek.wsb.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Market {
    private final static int INITIAL_PROJECT_COUNT = 3;
    private final static int TRIES_TO_NEW_PROJECT = 2; //5;
    private static Market instance = new Market();

    public static Market getInstance() {
        return instance;
    }

    public List<Client> clients = new ArrayList<>();
    public List<Project> projects = new ArrayList<>();
    private int counter = 0;

    private Market() {
        for (int i = 0; i < INITIAL_PROJECT_COUNT; i++)
            getNewProject();
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

    private Project getNewProject() {
        Client cl = getRandomClient();
        Project p = Project.generateNewProject(cl);
        projects.add(p);
        return p;
    }

    public void showAvailableProjects() {
        for (Project p : projects)
            System.out.println(p);
    }

    public void searchForNewProject() {
        if (++counter % TRIES_TO_NEW_PROJECT == 0) {
            Project p = getNewProject();
            System.out.println(p);
        }

    }
}
