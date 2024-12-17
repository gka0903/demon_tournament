package org.example.card;

import javax.swing.*;

public class Card {
    protected CardData cardData;  // 카드 데이터
    protected ImageIcon cardImage;  // 카드 이미지

    // 생성자: CardData를 받아 초기화
    public Card(CardData cardData) {
        this.cardData = cardData;
        this.cardImage = new ImageIcon(cardData.getCardSpritePath());
    }

    // 카드 타입 반환
    public CardType getCardType() {
        return cardData.getCardType();
    }

    // 카드 이미지를 JLabel로 반환
    public JLabel getCardImageLabel() {
        JLabel label = new JLabel(cardImage);
        return label;
    }

    public CardData getCardData() {
        return cardData;
    }

    public ImageIcon getCardImage() {
        return cardImage;
    }
}
