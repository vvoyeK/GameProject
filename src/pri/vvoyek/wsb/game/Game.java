package pri.vvoyek.wsb.game;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

public class Game {

    private static Random r = new Random(Settings.GAME_SEED);

    public static int nextInt(int n) {
        return r.nextInt(n);
    }

    private interface Action {
        boolean matches(String input);
        boolean action(String input);
        String help();
    }

    private abstract class AbstractAction implements Action {
        protected final String regex;
        protected final String help;
        protected String[] words;

        protected AbstractAction(String regex, String help) {
            this.regex = regex;
            this.help = help;
        }

        public boolean matches(String input) {
            if (!Pattern.matches(regex, input)) {
                return false;
            }
            words = input.split(" ");
            return true;
        }

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
            for (Action a : moves) {
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
            gameOver("Zakończono grę!");
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

    private class SearchProject extends AbstractAction {
        public SearchProject() {
            super("search", "search : przeznacza dzień na szukanie nowych projektów");
        }
        public boolean action(String input) {
            market.searchForNewProject();
            return true;
        }
    }

    private class SignContract extends AbstractAction {
        public SignContract() {
            super("sign[ \t\n\r].*", "sign NAME : podpisanie kontraktu na projekt NAME");
        }
        public boolean action(String input) {
            if (words.length != 2) {
                System.out.println(help);
                return false;
            }
            String projectName = words[1];
            Project project = market.findProject(projectName);
            if (project == null) {
                System.out.println("Nie ma takiego projektu: " + projectName);
                return false;
            }
            if (project.isComplex() && !company.hasEmployees()) {
                System.out.println("Nie dasz rady zrobić " + projectName);
                return false;
            }
            market.projects.remove(project);
            company.projects.add(project);
            System.out.println("Podpisano kontrakt na projekt " + project);
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

    private class ShowCash extends AbstractAction {
        public ShowCash() {
            super("cash", "cash : pokazuje aktualny stan konta");
        }
        public boolean action(String input) {
            company.showCash();
            return true;
        }
    }

    private class WorkOnProject extends AbstractAction {
        public WorkOnProject() {
            super("work", "work : pracuj nad projektem");
        }
        public boolean action(String input) {
            for (Project p : company.projects) {
                if (p.doTheJob(company.owner)) {
                    return true;
                }
            }
            return true;
        }
    }

    private class TestSoftware extends AbstractAction {
        public TestSoftware() {
            super("test", "test : przeznacza dzień na testowanie oprogramowania");
        }
        public boolean action(String input) {
            for (Project p : company.projects) {
                if (p.hasBugs()) {
                    p.debug();
                    break;
                }
            }
            return true;
        }
    }

    private class DeliverProject extends AbstractAction {
        public DeliverProject() {
            super("deliver[ \t\n\r].*", "deliver : oddanie gotowego projektu");
        }
        public boolean action(String input) {
            if (words.length != 2) {
                System.out.println(help);
                return false;
            }
            String projectName = words[1];
            Project project = company.findProject(projectName);
            if (project == null) {
                System.out.println("Nie ma takiego projektu: " + projectName);
                return false;
            }
            if (!project.isDone()) {
                System.out.println("Projekt " + projectName + " nie jest gotowy");
                return false;
            }
            company.projects.remove(project);
            System.out.println("Oddano projekt " + projectName);
            return true;
        }
    }

    private class ShowAvailableEmployees extends AbstractAction {
        public ShowAvailableEmployees() {
            super("interview", "interview : pokaż listę dostępnych pracowników");
        }
        public boolean action(String input) {
            market.showAvailableEmployees();
            return true;
        }
    }

    private class SearchAvailableEmployees extends AbstractAction {
        public SearchAvailableEmployees() {
            super("pay headhunter", "pay headhunter : zapłać " + Settings.HEADHUNTER_COST + " za szukanie nowych pracowników");
        }
        public boolean action(String input) {
            company.cash -= Settings.HEADHUNTER_COST;
            market.addNewEmployee();
            return true;
        }
    }

    private class HireNewEmployee extends AbstractAction {
        public HireNewEmployee() { super("hire[ \t\n\r].*", "hire NAME : zatrudnia pracownika NAME do firmy"); }
        public boolean action(String input) {
            if (words.length != 2) {
                System.out.println(help);
                return false;
            }
            String employeeName = words[1];
            Employee employee = market.findEmployee(employeeName);
            if (employee == null) {
                System.out.println("Nie ma takiego pracownika: " + employeeName);
                return false;
            }
            market.employees.remove(employee);
            company.employees.add(employee);
            company.cash -= Settings.EMPLOYMENT_COST;
            System.out.println("Zatrudniono " + employeeName);
            return true;
        }
    }

    private class FireEmployee extends AbstractAction {
        public FireEmployee() {
            super("fire[ \t\n\r].*", "fire NAME : zwalnia pracownika NAME z firmy");
        }
        public boolean action(String input) {
            if (words.length != 2) {
                System.out.println(help);
                return false;
            }
            String employeeName = words[1];
            Employee employee = company.findEmployee(employeeName);
            if (employee == null) {
                System.out.println("Nie ma takiego pracownika: " + employeeName);
                return false;
            }
            company.employees.remove(employee);
            market.employees.add(employee);
            company.cash -= Settings.LAY_OFF_COST;
            System.out.println("Zwolniono " + employeeName);
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

    private class DoTaxes extends AbstractAction {
        public DoTaxes() {
            super("taxes", "taxes : przeznacza dzień na rozliczanie z urzędami");
        }
        public boolean action(String input) {
            company.taxDays ++;
            return true;
        }
    }

    private LocalDateTime today = Settings.START_OF_THE_GAME;
    private String finalScore = "";
    private List<Action> handlers = new ArrayList<>();
    private List<Action> moves = new ArrayList<>();
    private Market market = Market.getInstance();
    private Company company = new Company();

    public Game() {
        handlers.add(new Help());
        handlers.add(new EndGame());

        handlers.add(new ShowAvailableProjects());
        handlers.add(new ShowAvailableEmployees());

        handlers.add(new ShowStaff());
        handlers.add(new ShowProjects());
        handlers.add(new ShowCash());

        moves.add(new SearchAvailableEmployees());
        moves.add(new SearchProject());
        moves.add(new SignContract());
        moves.add(new WorkOnProject());
        moves.add(new TestSoftware());
        moves.add(new DeliverProject());
        moves.add(new HireNewEmployee());
        moves.add(new FireEmployee());
        moves.add(new DoTaxes());
    }

    public boolean isOver() {
        return !finalScore.isEmpty();
    }

    public void gameOver(String reason) {
        finalScore = "GAME OVER: " + reason;
        System.out.println(finalScore);
    }

    public void process(String input) {
        for (Action a : handlers) {
            if (a.matches(input)) {
                a.action(input);
                return;
            }
        }

        for (Action a : moves) {
            if (a.matches(input)) {
                if (a.action(input)) {
                    endOfDay();
                }
                return;
            }
        }

        System.out.println("Nie rozumiem.");
    }

    private static boolean isWorkday(LocalDateTime date) {
        if (date.getDayOfWeek().equals(DayOfWeek.SATURDAY) || date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            return false;
        }
        for (LocalDateTime h : Settings.HOLIDAYS) {
            if (h.getMonth().equals(date.getMonth()) && h.getDayOfMonth() == date.getDayOfMonth())
                return false;
        }
        return true;
    }

    private void endOfDay() {
        LocalDateTime tomorrow = today.plusDays(1);

        if (isWorkday(today)) {
            employeesAtWork();
        }

        payFixedCosts();
        paySalaries();

        if (company.cash < 0.0) {
            gameOver("zabrakło gotówki, przegrałeś");
            return;
        }

        if (tomorrow.getDayOfMonth() == 1) {
            if (company.taxDays < 2) {
                gameOver("kontrola ZUS, przegrałeś");
                return;
            }
            company.taxDays = 0;
        }

        today = tomorrow;
        System.out.println("Nowy dzień! " + today + " " + today.getDayOfWeek());
    }

    private void employeesAtWork() {
        for (Programmer student : company.students) {
            for (Project p : company.projects) {
                if (p.doTheJob(student)) {
                    System.out.println("Student " + student.name + " pracował nad " + p.name);
                    if (p.isDone()) {
                        System.out.println("Projekt " + p.name + " jest gotowy!");
                    }
                    break;
                }
            }
        }

        for (Employee e : company.employees) {
            if (e.isSick()) {
                System.out.println(e.name + " jest chory i nie może pracować!");
                continue;
            }
            if (e instanceof Salesman) {
                market.searchForNewProject();
                continue;
            }
            for (Project p : company.projects) {
                if (e instanceof Programmer) {
                    Programmer programmer = (Programmer) e;
                    if (p.doTheJob(programmer)) {
                        System.out.println("Programista " + programmer.name + " pracował nad " + p.name);
                        if (p.isDone()) {
                            System.out.println("Projekt " + p.name + " jest gotowy!");
                        }
                        break;
                    }
                } else if (e instanceof Tester) {
                    //
                }
            }
        }
    }

    private void payFixedCosts() {
        for (Employee e : company.employees) {
            company.cash -= Settings.EMPLOYEE_FIXED_COST;
        }
    }

    private void paySalaries() {
        List<Employee> leavingEmployees = new ArrayList<>();
        for (Employee e : company.employees) {
            if (company.cash > e.salary) {
                company.cash -= e.salary;
            } else {
                company.cash = 0.0;
                leavingEmployees.add(e);
            }
        }
        for (Employee e : leavingEmployees) {
            company.employees.remove(e);
            market.employees.add(e);
            System.out.println(e.name + " odszedł z firmy");
        }
        for (Employee e : company.employees) {
            company.cash -= e.salary * Settings.EMPLOYEE_SOCIAL_TAX_RATE;
        }
    }
}
