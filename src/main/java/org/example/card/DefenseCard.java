package org.example.card;

public class DefenseCard extends Card {
    // 생성자
    public DefenseCard(CardData cardData) {
        super(cardData);
    }

    // 피해 감소량 반환
    public int getReducedDamage() {
        return -cardData.getDamage(); // cardData의 음수로
    }
}
