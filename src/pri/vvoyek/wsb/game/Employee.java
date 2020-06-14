package pri.vvoyek.wsb.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
}
