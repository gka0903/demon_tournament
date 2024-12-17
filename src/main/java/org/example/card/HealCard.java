package org.example.card;

public class HealCard extends Card {
    // 생성자
    public HealCard(CardData cardData) {
        super(cardData);
    }

    // 스태미나 회복량 반환
    public int getHealAmount() {
        return cardData.getStamina(); // cardData의 Stamina 값을 양수로 반환
    }
}
