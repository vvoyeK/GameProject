package pri.vvoyek.wsb.game;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

public class Game {

    private static Random r = new Random(Settings.GAME_SEED);

    public static int nextInt(int n) {
        return r.nextInt(n);
    }

    public static double nextFairDouble(double min, double max) {
        return Double.max(min, Double.min(max, min + (max - min) / 2 + r.nextGaussian() * (max - min) / 2));
    }

    private interface Action {
        boolean matches(String input);
        boolean action();
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
        public boolean action() {
            for (Action a : moves) {
                System.out.println(a.help());
            }
            return false;
        }
    }

    private class EndGame extends AbstractAction {
        public EndGame() {
            super("quit", "quit : kończy bieżącą rozgrywkę");
        }
        public boolean action() {
            gameOver("Zakończono grę!");
            return false;
        }
    }

    private class ShowAvailableProjects extends AbstractAction {
        public ShowAvailableProjects() {
            super("market","market : wyświetla listę dostępnych projektów");
        }
        public boolean action() {
            market.showAvailableProjects();
            return false;
        }
    }

    private class SearchProject extends AbstractAction {
        public SearchProject() {
            super("search", "search : przeznacza dzień na szukanie nowych projektów");
        }
        public boolean action() {
            market.searchForNewProject();
            return true;
        }
    }

    private class SignContract extends AbstractAction {
        public SignContract() {
            super("sign[ \t\n\r].*", "sign NAME : podpisanie kontraktu na projekt NAME");
        }
        public boolean action() {
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
            project.contractor = company;
            project.deadline = today.plusDays(project.daysForDelivery());
            System.out.println("Podpisano kontrakt na projekt " + project);
            if (project.getDownPayment() != 0.0) {
                company.receivePayment(project.getDownPayment(), projectName);
                project.payment = project.getDownPayment();
            }
            return true;
        }
    }

    private class ShowProjects extends AbstractAction {
        public ShowProjects() {
            super("projects", "projects : pokazuje aktualne projekty firmy");
        }
        public boolean action() {
            company.showProjects();
            return false;
        }
    }

    private class ShowCash extends AbstractAction {
        public ShowCash() {
            super("cash", "cash : pokazuje aktualny stan konta");
        }
        public boolean action() {
            company.showCash();
            return false;
        }
    }

    private class WorkOnProject extends AbstractAction {
        public WorkOnProject() {
            super("work", "work : pracuj nad projektem");
        }
        public boolean action() {
            for (Project p : company.projects) {
                if (p.doTheJob(company.owner, false)) {
                    System.out.println(company.owner.name + " pracował nad " + p.name);
                    break;
                }
            }
            return true;
        }
    }

    private class TestSoftware extends AbstractAction {
        public TestSoftware() {
            super("test", "test : przeznacza dzień na testowanie oprogramowania");
        }
        public boolean action() {
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
        public boolean action() {
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
            company.projectsDone.add(project);
            project.owner.projects.add(project);
            project.deliveryDate = today;
            project.paymentDate = today.plusDays(project.paymentDelay + project.owner.getExtraPaymentDelay());
            System.out.println("Oddano projekt " + projectName);
            return true;
        }
    }

    private class ShowAvailableEmployees extends AbstractAction {
        public ShowAvailableEmployees() {
            super("interview", "interview : pokaż listę dostępnych pracowników");
        }
        public boolean action() {
            market.showAvailableEmployees();
            return false;
        }
    }

    private class SearchAvailableEmployees extends AbstractAction {
        public SearchAvailableEmployees() {
            super("pay headhunter", "pay headhunter : zapłać " + Settings.HEADHUNTER_COST + " za szukanie nowych pracowników");
        }
        public boolean action() {
            company.cash -= Settings.HEADHUNTER_COST;
            market.addNewEmployee();
            return true;
        }
    }

    private class HireNewEmployee extends AbstractAction {
        public HireNewEmployee() { super("hire[ \t\n\r].*", "hire NAME : zatrudnia pracownika NAME do firmy"); }
        public boolean action() {
            if (words.length != 2) {
                System.out.println(help);
                return false;
            }
            String employeeName = words[1];
            Employee employee = market.findEmployee(employeeName);
            if (employee == null) {
                employee = company.findStudent(employeeName);
                if (employee == null) {
                    System.out.println("Nie ma takiego pracownika: " + employeeName);
                    return false;
                }
                company.employees.add(employee);
                System.out.println("Zlecono pracę " + employeeName);
                return false; // same day ?
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
        public boolean action() {
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
            if (company.students.contains(employee)) {
                System.out.println("Zwolniono " + employeeName);
                return false; // same day
            }
            market.employees.add(employee);
            company.cash -= Settings.LAY_OFF_COST;
            System.out.println("Zwolniono " + employeeName);
            return true;
        }
    }

    private class ShowStaff extends AbstractAction {
        public ShowStaff() {
            super("staff", "staff : pokazuje ludzi w firmie");
        }
        public boolean action() {
            company.showStaff();
            return false;
        }
    }

    private class ShowEmployees extends AbstractAction {
        public ShowEmployees() {
            super("employees", "employees : pokazuje ludzi pracujących w firmie");
        }
        public boolean action() {
            company.showEmployees();
            return false;
        }
    }

    private class DoTaxes extends AbstractAction {
        public DoTaxes() {
            super("taxes", "taxes : przeznacza dzień na rozliczanie z urzędami");
        }
        public boolean action() {
            company.taxDays ++;
            return true;
        }
    }

    private LocalDate today = Settings.GAME_START_DAY;
    private String finalScore = "";
    private List<Action> moves = new ArrayList<>();
    private Market market = Market.getInstance();
    private Company company = new Company();

    public Game() {
        moves.add(new Help());
        moves.add(new ShowAvailableProjects());
        moves.add(new ShowAvailableEmployees());
        moves.add(new ShowStaff());
        moves.add(new ShowEmployees());
        moves.add(new ShowProjects());
        moves.add(new ShowCash());

        moves.add(new SearchAvailableEmployees());
        moves.add(new SearchProject());
        moves.add(new SignContract());
        moves.add(new WorkOnProject());
        moves.add(new TestSoftware());
        moves.add(new DeliverProject());
        moves.add(new HireNewEmployee());
        moves.add(new FireEmployee());
        moves.add(new DoTaxes());
        moves.add(new EndGame());
    }

    public boolean isOver() {
        return !finalScore.isEmpty();
    }

    public void gameOver(String reason) {
        finalScore = "GAME OVER: " + reason;
        System.out.println(finalScore);
    }

    public void process(String input) {

        for (Action a : moves) {
            if (a.matches(input)) {
                if (a.action()) {
                    endOfDay();
                }
                return;
            }
        }

        System.out.println("Nie rozumiem.");
    }

    private void endOfDay() {
        LocalDate tomorrow = today.plusDays(1);

        if (Settings.isWorkday(today)) {
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

        clientsPayBills();

        if (isEndOfGame()) {
            gameOver("Gratulacje, wygrałeś!");
            return;
        }

        today = tomorrow;
        System.out.println("Nowy dzień! " + today + " " + today.getDayOfWeek());
    }

    private boolean isEndOfGame() {
        int complexProjectCount = 0;
        for (Project p : company.projectsDone) {
            if (p.isComplex() && p.wasFullyPaid()) {
                complexProjectCount ++;
            }
        }

        return complexProjectCount > 3 &&
                company.cash > Settings.COMPANY_INITIAL_CASH;
    }

    private void employeesAtWork() {
        for (Employee e : company.employees) {
            if (e.isSick()) {
                System.out.println(e.name + " jest chory i nie może pracować!");
                continue;
            }
            if (e instanceof Salesman) {
                System.out.println(e.name + " szuka nowych projektów");
                market.searchForNewProject();
                continue;
            }
            if (e instanceof Tester) {
                System.out.println(e.name + " testuje");
                continue;
            }
            if (e instanceof Programmer) {
                Programmer programmer = (Programmer) e;
                boolean tested = company.hasTesterCoverage();
                for (Project p : company.projects) {
                    if (p.doTheJob(programmer, tested)) {
                        System.out.println(e.name + " pracował nad " + p.name);
                        if (p.isDone()) {
                            System.out.println("Projekt " + p.name + " jest gotowy!");
                        }
                        break;
                    }
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

    private void clientsPayBills() {
        for (Client c : market.clients) {
            c.payBills(today);
        }
    }
}
