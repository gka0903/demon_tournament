package org.example;

import org.example.game.Game;
import org.example.players.Player;

public class Main {
    public static void main(String[] args) {
        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");

        Game game = new Game(player1, player2);
        game.start();
    }
}
