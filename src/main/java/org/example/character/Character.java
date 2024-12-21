package org.example.character;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.example.card.Card;
import java.util.Queue;

public class Character {
    private String name;
    private Image idleImage;    // 정지 상태 이미지
    private Image moveImage;    // 이동 상태 이미지
    private Image healImage;    // 회복 상태 이미지
    private Image defenseImage; // 방어 상태 이미지
    private Image currentImage; // 현재 상태 이미지
    private Point gridPosition; // 현재 그리드 위치
    private boolean isHealing;  // 회복 상태 플래그
    private boolean isDefending; // 방어 상태 플래그
    private Image hitImage;
    private Image attack1Image;
    private Image attack2Image;
    private List<Card> cardList;
    private final int MAX_CARDS = 3; // 카드 큐 최대 크기

    private int health;         // 캐릭터 체력 (HP)
    private int stamina;        // 캐릭터 스태미나 (MP)
    private final int maxHealth; // 체력 최대치
    private final int maxStamina; // 스태미나 최대치

    public Character(String name, String idleImagePath, String moveImagePath, String healImagePath, String defenseImagePath, String attack1ImagePath, String attack2ImagePath, String hitImagePath, Point initialGridPosition) {
        this.name = name;
        this.idleImage = Toolkit.getDefaultToolkit().createImage(idleImagePath);
        this.moveImage = Toolkit.getDefaultToolkit().createImage(moveImagePath);
        this.healImage = Toolkit.getDefaultToolkit().createImage(healImagePath);
        this.defenseImage = Toolkit.getDefaultToolkit().createImage(defenseImagePath);
        this.attack1Image = Toolkit.getDefaultToolkit().createImage(attack1ImagePath);
        this.attack2Image = Toolkit.getDefaultToolkit().createImage(attack2ImagePath);
        this.hitImage = Toolkit.getDefaultToolkit().createImage(hitImagePath);
        this.currentImage = idleImage;
        this.gridPosition = initialGridPosition;
        this.isHealing = false;
        this.isDefending = false;
        this.cardList = new ArrayList<>();

        // 기본 체력과 스태미나 값 설정
        this.maxHealth = 100;
        this.maxStamina = 100;
        this.health = maxHealth;
        this.stamina = maxStamina;
    }

    // 카드 추가 메서드
    public void addCard(Card card) {
        if (cardList.size() >= MAX_CARDS) {
            cardList.remove(0); // 리스트가 꽉 차면 가장 오래된 카드를 제거
        }
        cardList.add(card);
    }


    // 현재 카드 사용 후 제거 메서드
    public Card useCard() {
        if (!cardList.isEmpty()) {
            return cardList.remove(0); // 가장 앞의 카드를 반환 후 제거
        }
        return null; // 사용 가능한 카드가 없으면 null 반환
    }

    // 카드가 남아있는지 확인
    public boolean hasCards() {
        return !cardList.isEmpty();
    }


    // 카드 리스트 조회 메서드
    public List<Card> getCardList() {
        return new ArrayList<>(cardList); // 리스트의 복사본 반환
    }

    // 상태 업데이트 메서드
    public void updateState(boolean isMoving) {
        if (isHealing) {
            currentImage = healImage;
        } else if (isDefending) {
            currentImage = defenseImage;
        } else if (isMoving) {
            currentImage = moveImage;
        } else {
            currentImage = idleImage;
        }
    }

    // 체력 관리 메서드
    public int getHealth() {
        return health;
    }

    public void heal(int amount) {
        if (amount > 0) {
            health = Math.min(health + amount, maxHealth); // 체력 회복 (최대 체력 초과 방지)
        }
    }

    public void takeDamage(int damage) {
        if (damage > 0) {
            health = Math.max(health - damage, 0); // 체력 감소 (최소 체력 0 유지)
        }
    }

    // 스태미나 관리 메서드
    public int getStamina() {
        return stamina;
    }

    public void restoreStamina(int amount) {
        if (amount > 0) {
            stamina = Math.min(stamina + amount, maxStamina); // 스태미나 회복 (최대 스태미나 초과 방지)
        }
    }

    public void consumeStamina(int amount) {
        if (amount > 0) {
            stamina = Math.max(stamina - amount, 0); // 스태미나 소비 (최소 스태미나 0 유지)
        }
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getMaxStamina() {
        return maxStamina;
    }

    public boolean isAlive() {
        return health > 0; // 캐릭터 생존 여부
    }

    public String getName() {
        return name;
    }

    public Image getHealImage() {
        return healImage;
    }

    public Image getDefenseImage() {
        return defenseImage;
    }

    public Image getHitImage() {
        return hitImage;
    }

    public boolean isHealing() {
        return isHealing;
    }

    public boolean isDefending() {
        return isDefending;
    }

    // Getter 및 Setter
    public Image getCurrentImage() {
        return currentImage;
    }

    public Image getMoveImage() {
        return moveImage;
    }

    public Image getIdleImage() {
        return idleImage;
    }

    public Image getAttack1Image() {
        return attack1Image;
    }

    public Image getAttack2Image() {
        return attack2Image;
    }

    public Point getGridPosition() {
        return gridPosition;
    }

    public void setGridPosition(Point newPosition) {
        this.gridPosition = newPosition;
    }

    public void setHealing(boolean healing) {
        isHealing = healing;
    }

    public void setDefending(boolean defending) {
        isDefending = defending;
    }
}
