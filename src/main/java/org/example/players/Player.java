package org.example.players;

import java.util.ArrayList;
import java.util.List;
import org.example.cards.Card;
import org.example.cards.CardFactory;
import org.example.cards.GuardCard;

public class Player {
    private String name;
    private int health = 100;
    private int energy = 100;
    private int positionX = 0;
    private int positionY = 0;
    private boolean isGuarding = false;
    private List<String> deck = new ArrayList<>();

    public Player(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }
    public int getEnergy() { return energy; }
    public void setEnergy(int energy) { this.energy = energy; }
    public int getPositionX() { return positionX; }
    public int getPositionY() { return positionY; }
    public void setPosition(int x, int y) {
        this.positionX = x;
        this.positionY = y;
    }
    public boolean isGuarding() { return isGuarding; }
    public void setGuarding(boolean isGuarding) { this.isGuarding = isGuarding; }

    public void chooseCards(List<String> cardNames) {
        deck.clear();
        deck.addAll(cardNames);
    }

    // 상대방과 카드를 사용해 대결하며, 방어는 한 번만 유효하도록 조정
    public String useNextCard(Player opponent) {
        if (deck.isEmpty()) return null;

        String cardName = deck.remove(0);
        Card card = CardFactory.createCard(cardName);

        // 카드 실행
        card.execute(this, opponent);

        // 만약 현재 카드가 GuardCard가 아니라면 방어 상태 해제
        if (!(card instanceof GuardCard)) {
            setGuarding(false);
        } else {
            setGuarding(true);  // GuardCard일 때는 방어 활성화
        }

        return card.getName();
    }

    public void resetTurn() {
        energy += 15;
        isGuarding = false;  // 턴이 끝날 때 방어 상태 초기화
    }
}
