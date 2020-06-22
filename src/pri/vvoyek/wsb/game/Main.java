package pri.vvoyek.wsb.game;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String[] players = new String[]{ "Player1" };
        if (args.length > 0)
            players = args;

        Game[] games = new Game[players.length];
        for (int i = 0; i < players.length; i++) {
            games[i] = new Game(players[i]);
        }

        Scanner scanner = new Scanner(System.in);

        boolean loop = true;
        while (loop) {
            loop = false;
            for (Game g : games) {
                if (g.isOver())
                    continue;

                System.out.print("\nCo chcesz zrobić? " + g + "> ");
                if (!scanner.hasNextLine())
                    return;

                String input = scanner.nextLine();
                g.process(input);
                loop = true;
            }
        }
    }
}
