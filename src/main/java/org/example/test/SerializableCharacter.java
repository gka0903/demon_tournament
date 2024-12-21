package org.example.test;

import org.example.card.CardData;

import java.awt.*;
import java.io.Serializable;
import java.util.List;

public class SerializableCharacter implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name; // 캐릭터 이름
    private String idleImagePath;
    private String moveImagePath;
    private String healImagePath;
    private String defenseImagePath;
    private String attack1ImagePath;
    private String attack2ImagePath;
    private Point gridPosition; // 그리드 위치
    private int health; // 체력
    private int stamina; // 스태미나
    private List<SerializableCardData> cardList; // 카드 리스트

    public SerializableCharacter(String name, String idleImagePath, String moveImagePath, String healImagePath,
                                  String defenseImagePath, String attack1ImagePath, String attack2ImagePath,
                                  Point gridPosition, int health, int stamina, List<SerializableCardData> cardList) {
        this.name = name;
        this.idleImagePath = idleImagePath;
        this.moveImagePath = moveImagePath;
        this.healImagePath = healImagePath;
        this.defenseImagePath = defenseImagePath;
        this.attack1ImagePath = attack1ImagePath;
        this.attack2ImagePath = attack2ImagePath;
        this.gridPosition = gridPosition;
        this.health = health;
        this.stamina = stamina;
        this.cardList = cardList;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public String getIdleImagePath() {
        return idleImagePath;
    }

    public String getMoveImagePath() {
        return moveImagePath;
    }

    public String getHealImagePath() {
        return healImagePath;
    }

    public String getDefenseImagePath() {
        return defenseImagePath;
    }

    public String getAttack1ImagePath() {
        return attack1ImagePath;
    }

    public String getAttack2ImagePath() {
        return attack2ImagePath;
    }

    public Point getGridPosition() {
        return gridPosition;
    }

    public int getHealth() {
        return health;
    }

    public int getStamina() {
        return stamina;
    }

    public List<SerializableCardData> getCardList() {
        return cardList;
    }
}
