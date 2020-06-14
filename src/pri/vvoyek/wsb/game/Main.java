package pri.vvoyek.wsb.game;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Game g = new Game();

        while (!g.isOver()) {
            System.out.println("Co chcesz zrobić?");
            //String input = System.console().readLine();
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            g.process(input);
        }
    }
}
