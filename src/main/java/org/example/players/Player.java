package org.example.players;

import org.example.cards.Card;
import org.example.cards.AttackCard;
import org.example.cards.GuardCard;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int health = 100;
    private int energy = 100;

    private int positionX;
    private int positionY;
    private boolean isGuarding = false;
    private List<Card> deck = new ArrayList<>();

    public Player(String name, int startX, int startY) {
        this.name = name;
        this.positionX = startX;
        this.positionY = startY;
    }

    public void move(int deltaX, int deltaY) {
        int newX = positionX + deltaX;
        int newY = positionY + deltaY;

        // 이동이 4x3 필드 안에 있는지 확인
        if (newX >= 0 && newX < 4 && newY >= 0 && newY < 3) {
            positionX = newX;
            positionY = newY;
            System.out.println(name + " moves to (" + positionX + ", " + positionY + ")");
        } else {
            System.out.println(name + " cannot move out of bounds.");
        }
    }

    public boolean isNextCardGuard() {
        // 덱에 카드가 있는지 확인하고 첫 번째 카드가 GuardCard인지 확인
        return !deck.isEmpty() && deck.get(0) instanceof GuardCard;
    }

    public boolean chooseCard(Card card) {
        if (deck.size() >= 3) {
            System.out.println(name + " already has 3 cards selected.");
            return false;
        }
        if (card instanceof AttackCard) {
            AttackCard attackCard = (AttackCard) card;
            if (energy >= attackCard.getEnergyCost()) {
                deck.add(attackCard);
                energy -= attackCard.getEnergyCost();
                System.out.println(name + " selected " + attackCard.getName() + ", costing " + attackCard.getEnergyCost() + " energy.");
                return true;
            } else {
                System.out.println(name + " does not have enough energy to select " + attackCard.getName() + ".");
                return false;
            }
        } else {
            deck.add(card);
            System.out.println(name + " selected " + card.getName());
            return true;
        }
    }
    public Card useNextCard(Player opponent) {
        if (deck.isEmpty()) return null;
        Card card = deck.remove(0);
        card.execute(this, opponent);
        return card;
    }

    public void resetTurn() {
        energy += 15;
        isGuarding = false;
        deck.clear();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public List<Card> getDeck() {
        return deck;
    }

    public void setDeck(List<Card> deck) {
        this.deck = deck;
    }

    public String getName() { return name; }
    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }
    public int getEnergy() { return energy; }
    public int getPositionX() { return positionX; }
    public int getPositionY() { return positionY; }
    public boolean isGuarding() { return isGuarding; }
    public void setGuarding(boolean isGuarding) { this.isGuarding = isGuarding; }
}
