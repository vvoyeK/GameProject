package pri.vvoyek.wsb.game;

import java.util.ArrayList;
import java.util.List;

public class Company {
    public double cash = Settings.COMPANY_INITIAL_CASH;
    public Programmer owner;
    public List<Programmer> students = new ArrayList<>();
    public List<Employee> employees = new ArrayList<>();
    public List<Project> projects = new ArrayList<>();
    public int taxDays = 0;

    public Company() {
        owner = new Programmer("myself", 0.0, new Technology[] {
                                Technology.DATABASE,
                                Technology.FRONT_END,
                                Technology.WORDPRESS,
                                Technology.PRESTASHOP },
                0, 0);
        students.add(new Programmer("Bob", 0.9 * Employee.getFairSalary(), Technology.getRandomTechnologies(), 0, 0));
        students.add(new Programmer("Mobby", 0.6 * Employee.getFairSalary(), Technology.getRandomTechnologies(), 10, 0));
        students.add(new Programmer("Lucky", 0.4 * Employee.getFairSalary(), Technology.getRandomTechnologies(), 20, 20));
        employees.add(new Programmer("ZORG", 0.0, Technology.values(), 0, 0));
    }

    public void showCash() {
        System.out.println("Obecne środki: " + cash);
    }

    public void showStaff() {
        System.out.println("Nasza załoga:");
        System.out.println(owner);
        for (Employee e : students)
            System.out.println(e);
    }

    public Programmer findStudent(String name) {
        for (Programmer p : students) {
            if (p.name.equals(name)) {
                return p;
            }
        }
        return null;
    }

    public boolean hasEmployees() {
        return !employees.isEmpty();
    }

    public Employee findEmployee(String name) {
        for (Employee e : employees) {
            if (e.name.equals(name)) {
                return e;
            }
        }
        return null;
    }

    public void showEmployees() {
        if (employees.isEmpty()) {
            System.out.println("Obecnie nie masz zatrudnionych pracowników!");
        } else {
            System.out.println("Pracownicy:");
            for (Employee e : employees)
                System.out.println(e + " dniówka " + e.salary);
        }
    }

    private int countTesters() {
        int count = 0;
        for (Employee e : employees) {
            if (e instanceof Tester)
                count++;
        }
        return count;
    }

    private int countProgrammers() {
        int count = 0;
        for (Employee e : employees) {
            if (e instanceof Programmer)
                count++;
        }
        return count;
    }

    public boolean hasTesterCoverage() {
        int testers = countTesters();
        if (testers == 0)
            return false;
        return countProgrammers() / testers <= 3;
    }

    public void showProjects() {
        System.out.println("Nasze projekty:");
        for (Project p : projects)
            System.out.println(p);
    }

    public Project findProject(String name) {
        for (Project p : projects) {
            if (p.name.equals(name)) {
                return p;
            }
        }
        return null;
    }

    public void receivePayment(double bill, String name) {
        System.out.println("Otrzymano zapłatę " + bill + " za " + name);
        cash += bill;
        showCash();
    }
}
