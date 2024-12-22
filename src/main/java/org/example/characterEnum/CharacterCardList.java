package org.example.characterEnum;

import java.util.List;
import org.example.card.AttackCard;
import org.example.card.AttackShape;
import org.example.card.Card;
import org.example.card.CardData;
import org.example.card.CardType;
import org.example.card.MoveDirection;

public enum CharacterCardList {
    INUYASHA,
    SESSHOMARU;

    // 각 캐릭터에 맞는 카드 목록을 반환하는 메서드
    public List<Card> getCards() {
        switch (this) {
            case INUYASHA:
                return List.of(
                        new Card(new CardData(CardType.MOVE, 0, 0, "src/main/resources/animations/cards/card/이누아래.png", "src/main/resources/animations/cards/카드뒷면.png", false, MoveDirection.DOWN, null)),
                        new Card(new CardData(CardType.MOVE, 0, 0, "src/main/resources/animations/cards/card/이누위.png", "src/main/resources/animations/cards/카드뒷면.png", false, MoveDirection.UP, null)),
                        new Card(new CardData(CardType.MOVE, 0, 0, "src/main/resources/animations/cards/card/이누오른쪽.png", "src/main/resources/animations/cards/카드뒷면.png", false, MoveDirection.RIGHT, null)),
                        new Card(new CardData(CardType.MOVE, 0, 0, "src/main/resources/animations/cards/card/이누왼쪽.png", "src/main/resources/animations/cards/카드뒷면.png", false, MoveDirection.LEFT, null)),
                        new Card(new CardData(CardType.DEFENSE, 0, 0, "src/main/resources/animations/cards/card/이누방어.png", "src/main/resources/animations/cards/카드뒷면.png", false, null, null)),
                        new Card(new CardData(CardType.HEAL, 0, 0, "src/main/resources/animations/cards/card/이누힐.png", "src/main/resources/animations/cards/카드뒷면.png", false, null, null)),
                        new AttackCard(new CardData(CardType.ATTACK1, 25, 25, "src/main/resources/animations/cards/card/이누공격2.png", "src/main/resources/animations/cards/카드뒷면.png", false, null, AttackShape.CROSS)),
                        new AttackCard(new CardData(CardType.ATTACK2, 50, 50, "src/main/resources/animations/cards/card/이누공격1.png", "src/main/resources/animations/cards/카드뒷면.png", false, null, AttackShape.T_DOWN))
                );
            case SESSHOMARU:
                return List.of(
                        new Card(new CardData(CardType.MOVE, 0, 0, "src/main/resources/animations/cards/card/셋쇼아래.png", "src/main/resources/animations/cards/카드뒷면.png", false, MoveDirection.DOWN, null)),
                        new Card(new CardData(CardType.MOVE, 0, 0, "src/main/resources/animations/cards/card/셋쇼위.png", "src/main/resources/animations/cards/카드뒷면.png", false, MoveDirection.UP, null)),
                        new Card(new CardData(CardType.MOVE, 0, 0, "src/main/resources/animations/cards/card/셋쇼오른쪽.png", "src/main/resources/animations/cards/카드뒷면.png", false, MoveDirection.RIGHT, null)),
                        new Card(new CardData(CardType.MOVE, 0, 0, "src/main/resources/animations/cards/card/셋쇼왼쪽.png", "src/main/resources/animations/cards/카드뒷면.png", false, MoveDirection.LEFT, null)),
                        new Card(new CardData(CardType.DEFENSE, 0, 0, "src/main/resources/animations/cards/card/셋쇼방어.png", "src/main/resources/animations/cards/카드뒷면.png", false, null, null)),
                        new Card(new CardData(CardType.HEAL, 0, 0, "src/main/resources/animations/cards/card/셋쇼힐.png", "src/main/resources/animations/cards/카드뒷면.png", false, null, null)),
                        new AttackCard(new CardData(CardType.ATTACK1, 25, 25, "src/main/resources/animations/cards/card/셋쇼공격2.png", "src/main/resources/animations/cards/카드뒷면.png", false, null, AttackShape.X)),
                        new AttackCard(new CardData(CardType.ATTACK2, 60, 50, "src/main/resources/animations/cards/card/셋쇼공격1.png", "src/main/resources/animations/cards/카드뒷면.png", false, null,
                                AttackShape.VERTICAL))
                );
            default:
                throw new IllegalArgumentException("Unknown character type: " + this);
        }
    }
}
