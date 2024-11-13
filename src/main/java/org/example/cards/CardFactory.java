package org.example.cards;

public class CardFactory {
    public static Card createCard(String cardName) {
        switch (cardName.toLowerCase()) {
            case "moveup":
                return new MoveCards.MoveUpCard();
            case "movedown":
                return new MoveCards.MoveDownCard();
            case "moveleft":
                return new MoveCards.MoveLeftCard();
            case "moveright":
                return new MoveCards.MoveRightCard();
            case "attack":
                return new AttackCard(15); // 기본 공격력
            case "guard":
                return new GuardCard();
            case "energy":
                return new EnergyCard(15); // 기본 회복량
            default:
                throw new IllegalArgumentException("Invalid card name: " + cardName);
        }
    }
}
