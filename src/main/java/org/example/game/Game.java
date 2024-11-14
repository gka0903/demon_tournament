package org.example.game;

import org.example.players.Player;

public class Game {
    private Player player1;
    private Player player2;
    private int turnCount = 1;
    private boolean gameOver = false;

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public int getTurnCount() {
        return turnCount;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void playTurn() {
        if (gameOver) {
            System.out.println("Game is already over.");
            return;
        }
        System.out.println("\n--- Turn " + turnCount + " ---");
        for (int i = 0; i < 3; i++) {
            boolean player1Guard = player1.isNextCardGuard();
            boolean player2Guard = player2.isNextCardGuard();

            if (!player1Guard && player2Guard) {
                System.out.println(player2.getName() + " uses card: " + player2.useNextCard(player1));
                if (checkVictory()) return;
                System.out.println(player1.getName() + " uses card: " + player1.useNextCard(player2));
                if (checkVictory()) return;
            } else {
                System.out.println(player1.getName() + " uses card: " + player1.useNextCard(player2));
                if (checkVictory()) return;
                System.out.println(player2.getName() + " uses card: " + player2.useNextCard(player1));
                if (checkVictory()) return;
            }
        }
        turnCount++;
        player1.resetTurn();
        player2.resetTurn();
    }

    private boolean checkVictory() {
        if (player1.getHealth() <= 0) {
            System.out.println(player2.getName() + " wins!");
            gameOver = true;
            return true;
        }
        if (player2.getHealth() <= 0) {
            System.out.println(player1.getName() + " wins!");
            gameOver = true;
            return true;
        }
        return false;
    }

    public void resetGame() {
        player1.setHealth(100);
        player2.setHealth(100);
        turnCount = 1;
        gameOver = false;
        player1.resetTurn();
        player2.resetTurn();
    }
}
