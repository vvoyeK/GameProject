package pri.vvoyek.wsb.game;

import java.util.ArrayList;
import java.util.List;

public class Company {
    public double cash = Settings.COMPANY_INITIAL_CASH;
    public Programmer owner;
    public List<Programmer> students = new ArrayList<>();
    public List<Employee> employees = new ArrayList<>();
    public List<Project> projects = new ArrayList<>();

    public Company() {
        owner = new Programmer("myself", new Technology[] {
                Technology.DATABASE,
                Technology.FRONT_END,
                Technology.WORDPRESS,
                Technology.PRESTASHOP,
        });
        students.add(new Programmer("Bob", Technology.values()));
        students.add(new Programmer("Mobby", Technology.values()));
        students.add(new Programmer("Lucky", Technology.values()));
    }

    public void showStaff() {
        System.out.println("Nasza załoga:");
        System.out.println(owner);
        for (Employee e : students)
            System.out.println(e);
        for (Employee e : employees)
            System.out.println(e);
    }

    public void showProjects() {
        System.out.println("Nasze projekty:");
        for (Project p : projects)
            System.out.println(p);
    }

    public void addProject(Project project) {
        projects.add(project);
        System.out.println("Podpisano kontrakt na projekt " + project);
    }
}
