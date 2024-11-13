package org.example.game;

import java.util.Arrays;
import java.util.Scanner;
import org.example.players.Player;

public class Game {
    private Player player1;
    private Player player2;
    private int turnCount = 1;

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (!checkVictory()) {
            System.out.println("\n--- Turn " + turnCount + " ---");

            System.out.println(player1.getName() + ", choose 3 cards (comma separated, e.g., moveup,attack,guard):");
            player1.chooseCards(Arrays.asList(scanner.nextLine().trim().toLowerCase().split(",")));

            System.out.println(player2.getName() + ", choose 3 cards (comma separated, e.g., moveup,attack,guard):");
            player2.chooseCards(Arrays.asList(scanner.nextLine().trim().toLowerCase().split(",")));

            playTurn();
            turnCount++;
            player1.resetTurn();
            player2.resetTurn();
        }
        scanner.close();
        System.out.println("Game Over");
    }

    private void playTurn() {
        for (int i = 0; i < 3; i++) {
            System.out.println("\n" + player1.getName() + " uses card: " + player1.useNextCard(player2));
            if (checkVictory()) return;

            System.out.println(player2.getName() + " uses card: " + player2.useNextCard(player1));
            if (checkVictory()) return;
        }
    }

    private boolean checkVictory() {
        if (player1.getHealth() <= 0) {
            System.out.println(player2.getName() + " wins!");
            return true;
        }
        if (player2.getHealth() <= 0) {
            System.out.println(player1.getName() + " wins!");
            return true;
        }
        return false;
    }
}
