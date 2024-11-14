package org.example.game;

import org.example.players.Player;
import java.util.Scanner;

public class Game {
    private Player player1;
    private Player player2;
    private int turnCount = 1;

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public int getTurnCount() {
        return turnCount;
    }

    public void setTurnCount(int turnCount) {
        this.turnCount = turnCount;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        while (!checkVictory()) {
            System.out.println("\n--- Turn " + turnCount + " ---");

            // 각 플레이어가 카드 선택
            player1.chooseCards(scanner);
            player2.chooseCards(scanner);

            playTurn();
            turnCount++;
            player1.resetTurn();
            player2.resetTurn();
        }
        scanner.close();
        System.out.println("Game Over");
    }

    private void playTurn() {
        // 각 플레이어의 카드 3장을 순서대로 실행
        for (int i = 0; i < 3; i++) {
            boolean player1Guard = player1.isNextCardGuard();
            boolean player2Guard = player2.isNextCardGuard();

           if (!player1Guard && player2Guard) {
                // player2가 가드 카드를 사용하고, player1은 가드 카드를 사용하지 않는 경우
                System.out.println(player2.getName() + " uses card: " + player2.useNextCard(player1));
                if (checkVictory()) return;

                System.out.println(player1.getName() + " uses card: " + player1.useNextCard(player2));
                if (checkVictory()) return;
            } else {
                // 기본 순서대로 사용
               System.out.println(player1.getName() + " uses card: " + player1.useNextCard(player2));
               if (checkVictory()) return;

               System.out.println(player2.getName() + " uses card: " + player2.useNextCard(player1));
               if (checkVictory()) return;
            }
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
