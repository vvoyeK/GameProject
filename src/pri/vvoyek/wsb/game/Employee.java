package pri.vvoyek.wsb.game;

public class Employee {
    public final String name;
    public double salary;

    public Employee(String name) {
        this.name = name;
        this.salary = 0.0;
    }

    public Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }

    public String toString() {
        return name;
    }
}
