package org.example.players;

import org.example.cards.Card;
import org.example.cards.CardFactory;
import org.example.cards.AttackCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.example.cards.GuardCard;

public class Player {
    private String name;
    private int health = 100;
    private int energy = 100;

    private int positionX;
    private int positionY;
    private boolean isGuarding = false;
    private List<Card> deck = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public boolean isGuarding() {
        return isGuarding;
    }

    public void setGuarding(boolean guarding) {
        isGuarding = guarding;
    }

    public List<Card> getDeck() {
        return deck;
    }

    public void setDeck(List<Card> deck) {
        this.deck = deck;
    }

    public Player(String name, int startX, int startY) {
        this.name = name;
        this.positionX = startX;
        this.positionY = startY;
    }

    public void chooseCards(Scanner scanner) {
        deck.clear();
        System.out.println(name + ", select 3 cards for this turn:");

        int selectedCardCount = 0;
        while (selectedCardCount < 3) {
            System.out.print("Choose card " + (selectedCardCount + 1) + " (e.g., moveup, basicattack, guard): ");
            String cardName = scanner.nextLine().trim().toLowerCase();
            Card card = CardFactory.createCard(cardName);

            // 에너지를 소모하는 카드인 경우 확인
            if (card instanceof AttackCard) {
                AttackCard attackCard = (AttackCard) card;
                if (energy >= attackCard.getEnergyCost()) {
                    deck.add(attackCard);
                    energy -= attackCard.getEnergyCost();
                    selectedCardCount++;
                    System.out.println(name + " selected " + attackCard.getName() + ", costing " + attackCard.getEnergyCost() + " energy.");
                } else {
                    System.out.println(name + " does not have enough energy to select " + attackCard.getName() + ". Choose a different card.");
                }
            } else {
                // 에너지를 소모하지 않는 카드
                deck.add(card);
                selectedCardCount++;
                System.out.println(name + " selected " + card.getName());
            }
        }
    }

    // 덱에서 다음 카드를 사용하고, 해당 카드를 실행하는 메서드
    public String useNextCard(Player opponent) {
        if (deck.isEmpty()) return null;  // 덱이 비어 있는 경우 null 반환

        Card card = deck.remove(0);  // 덱에서 첫 번째 카드를 제거하고 사용
        card.execute(this, opponent);  // 카드의 동작을 실행
        return card.getName();  // 사용한 카드의 이름 반환
    }

    public void move(int deltaX, int deltaY) {
        int newX = positionX + deltaX;
        int newY = positionY + deltaY;

        if (newX >= 0 && newX <= 3 && newY >= 0 && newY <= 2) {
            positionX = newX;
            positionY = newY;
            System.out.println(name + " moves to (" + positionX + ", " + positionY + ")");
        } else {
            System.out.println(name + " cannot move out of bounds.");
        }
    }

    public boolean isNextCardGuard() {
        return !deck.isEmpty() && deck.get(0) instanceof GuardCard;
    }

    public void resetTurn() {
        energy += 15;
        isGuarding = false;
    }
}
