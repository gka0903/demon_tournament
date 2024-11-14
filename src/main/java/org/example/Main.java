package org.example;

import org.example.game.Game;
import org.example.players.Player;

public class Main {
    public static void main(String[] args) {
        // 두 플레이어 초기 위치 설정
        Player player1 = new Player("Player 1", 0, 1);
        Player player2 = new Player("Player 2", 3, 1);

        // 게임 시작
        Game game = new Game(player1, player2);
        game.start();
    }
}
