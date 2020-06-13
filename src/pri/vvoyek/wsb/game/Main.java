package pri.vvoyek.wsb.game;

import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Date today = new Date(2020, 6, 29);

        System.out.println(today);

        Calendar c = Calendar.getInstance();
        c.setTime(today);

        System.out.println(c.toString());
        c.add(Calendar.DAY_OF_MONTH, 10);

        System.out.println(c.toString());

        Game g = new Game();

        boolean loop = true;
        while (loop) {
            System.out.println("Co chcesz zrobić?");
            //String input = System.console().readLine();
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            g.process(input);
        }
    }
}
