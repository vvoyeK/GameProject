package pri.vvoyek.wsb.game;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Game {
    private interface Action {
        boolean process(String input);
        String help();
    }

    private abstract class AbstractAction implements Action {
        protected String regex;
        protected String help;

        protected AbstractAction(String regex, String help) {
            this.regex = regex;
            this.help = help;
        }

        public boolean process(String input) {
            if (!Pattern.matches(regex, input))
                return false;
            return action(input);
        }
        protected abstract boolean action(String input);

        public String help() {
            return help;
        }
    }

    private class Help extends AbstractAction {
        public Help() {
            super("help", "help: wyświetla listę dostępnych poleceń");
        }
        public boolean action(String input) {
            for (Action a : handlers) {
                System.out.println(a.help());
            }
            return true;
        }
    }

    private class EndGame extends AbstractAction {
        public EndGame() {
            super("quit", "quit : kończy bieżącą rozgrywkę");
        }
        public boolean action(String input) {
            System.out.println("Koniec gry!");
            return true;
        }
    }

    private class ShowAvailableProjects extends AbstractAction {
        public ShowAvailableProjects() {
            super("market","market : wyświetla listę dostępnych projektów");
        }
        public boolean action(String input) {
            market.showAvailableProjects();
            return true;
        }
    }

    private class SignContract extends AbstractAction {
        public SignContract() {
            super("sign[ \t\n\r].*", "sign NAME : podpisanie kontraktu na projekt o nazwie NAME");
        }
        public boolean action(String input) {
            String[] words = input.split(" ");
            if (words.length != 2) {
                System.out.println(help);
                return false;
            }
            String projectName = words[1];
            Project project = market.getProject(projectName);
            if (project == null) {
                System.out.println("Nie ma takiego projektu: " + projectName);
                return false;
            }
            company.addProject(project);
            return true;
        }
    }

    private class ShowAvailableEmployees extends AbstractAction {
        public ShowAvailableEmployees() {
            super("interview", "interview : pokaż listę dostępnych pracowników");
        }
        public boolean action(String input) {
            market.addNewEmployee();
            market.showAvailableEmployees();
            return true;
        }
    }

    private class ShowStaff extends AbstractAction {
        public ShowStaff() {
            super("staff", "staff : pokazuje ludzi pracujących w firmie");
        }
        public boolean action(String input) {
            company.showStaff();
            return true;
        }
    }

    private class ShowProjects extends AbstractAction {
        public ShowProjects() {
            super("projects", "projects : pokazuje aktualne projekty firmy");
        }
        public boolean action(String input) {
            company.showProjects();
            return true;
        }
    }

    private List<Action> handlers = new ArrayList<>();
    private Market market = Market.getInstance();
    private Company company = new Company();

    public Game() {
        handlers.add(new Help());
        handlers.add(new ShowAvailableProjects());
        handlers.add(new ShowAvailableEmployees());
        handlers.add(new SignContract());
        handlers.add(new ShowStaff());
        handlers.add(new ShowProjects());
        handlers.add(new EndGame());
    }

    public void process(String input) {
        for (Action a : handlers) {
            if (a.process(input))
                return;
        }
        System.out.println("Nie rozumiem.");
    }
}
