package pri.vvoyek.wsb.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Employee {
    public final String name;
    public double salary;

    public Employee(String name) {
        this.name = name;
        this.salary = getRandomSalary();
    }

    public Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }

    public String toString() {
        return name;
    }

    public boolean isSick() {
        return Game.nextInt(365) < Settings.SICK_DAYS_PER_YEAR;
    }

    private static String[] names = new String[] {
            "Antoni", "Bianca", "Cina", "Daria",
            "Eryk", "Filip", "Greta", "Henryk",
            "Irma", "Józef", "Klara", "Leon", "Mira",
            "Nadia", "Oleg", "Piotr", "Rufus", "Tina",
            "Ula", "Wiktor", "Zenon",
    };
    private static int[] surnameIndex = new int[names.length];

    public static String getNextName() {
        int index = Game.nextInt(names.length);
        return  names[index] + String.valueOf((char)('A' + surnameIndex[index]++));
    }

    public static double getFairSalary() {
        return Settings.EMPLOYEE_MIN_WAGE + (Settings.EMPLOYEE_MAX_WAGE - Settings.EMPLOYEE_MIN_WAGE)/2;
    }

    public static double getRandomSalary() {
        return Math.round(Game.nextFairDouble(Settings.EMPLOYEE_MIN_WAGE, Settings.EMPLOYEE_MAX_WAGE));
    }
}
