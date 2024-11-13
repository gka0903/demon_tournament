package org.example.players;

import java.util.ArrayList;
import java.util.List;
import org.example.cards.Card;
import org.example.cards.CardFactory;

public class Player {
    private String name;
    private int health = 100;
    private int energy = 100;

    // 초기 위치 설정
    private int positionX;
    private int positionY;

    // 필드의 경계를 나타내는 상수
    private static final int MAX_X = 3;
    private static final int MAX_Y = 2;
    private static final int MIN_X = 0;
    private static final int MIN_Y = 0;

    private boolean isGuarding = false;
    private List<String> deck = new ArrayList<>();

    public Player(String name, int startX, int startY) {
        this.name = name;
        this.positionX = startX;
        this.positionY = startY;
    }

    public String getName() { return name; }
    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }
    public int getEnergy() { return energy; }
    public void setEnergy(int energy) { this.energy = energy; }
    public int getPositionX() { return positionX; }
    public int getPositionY() { return positionY; }

    public boolean isGuarding() { return isGuarding; }
    public void setGuarding(boolean isGuarding) { this.isGuarding = isGuarding; }

    public void chooseCards(List<String> cardNames) {
        deck.clear();
        deck.addAll(cardNames);
    }

    // 이동 가능한 범위 안에서만 위치를 업데이트하는 메서드
    public void move(int deltaX, int deltaY) {
        int newX = positionX + deltaX;
        int newY = positionY + deltaY;

        // 필드 범위 안에 있을 때만 위치를 업데이트
        if (newX >= MIN_X && newX <= MAX_X && newY >= MIN_Y && newY <= MAX_Y) {
            positionX = newX;
            positionY = newY;
            System.out.println(name + " moves to (" + positionX + ", " + positionY + ")");
        } else {
            System.out.println(name + " cannot move out of bounds (" + newX + ", " + newY + ")");
        }
    }

    public String useNextCard(Player opponent) {
        if (deck.isEmpty()) return null;

        String cardName = deck.remove(0);
        Card card = CardFactory.createCard(cardName);

        // 카드 실행
        card.execute(this, opponent);

        // Guard 상태 관리
        if (!(card instanceof org.example.cards.GuardCard)) {
            setGuarding(false);
        } else {
            setGuarding(true);
        }

        return card.getName();
    }

    public void resetTurn() {
        energy += 15;
        isGuarding = false;
    }
}
