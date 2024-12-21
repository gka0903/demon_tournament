package org.example.test;

import org.example.card.CardType;
import org.example.card.MoveDirection;
import org.example.card.AttackShape;

import java.io.Serializable;

public class SerializableCardData implements Serializable {
    private static final long serialVersionUID = 1L;

    private CardType cardType; // 카드 타입
    private int damage; // 피해량
    private int stamina; // 스태미나 소모
    private String cardSpritePath; // 카드 이미지 경로
    private String cardBackSpritePath; // 카드 뒷면 경로
    private MoveDirection moveDir; // 이동 방향 (이동 카드용)
    private AttackShape atkShape; // 공격 형태 (공격 카드용)

    public SerializableCardData(CardType cardType, int damage, int stamina, String cardSpritePath,
                                String cardBackSpritePath, MoveDirection moveDir, AttackShape atkShape) {
        this.cardType = cardType;
        this.damage = damage;
        this.stamina = stamina;
        this.cardSpritePath = cardSpritePath;
        this.cardBackSpritePath = cardBackSpritePath;
        this.moveDir = moveDir;
        this.atkShape = atkShape;
    }

    // Getters and Setters
    public CardType getCardType() {
        return cardType;
    }

    public int getDamage() {
        return damage;
    }

    public int getStamina() {
        return stamina;
    }

    public String getCardSpritePath() {
        return cardSpritePath;
    }

    public String getCardBackSpritePath() {
        return cardBackSpritePath;
    }

    public MoveDirection getMoveDir() {
        return moveDir;
    }

    public AttackShape getAtkShape() {
        return atkShape;
    }
}
