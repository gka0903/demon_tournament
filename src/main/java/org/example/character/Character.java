package org.example.character;

import java.awt.*;
import java.util.LinkedList;
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
    private Queue<Card> cardQueue; // 캐릭터가 가진 카드 큐
    private final int MAX_CARDS = 3; // 카드 큐 최대 크기

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
        this.cardQueue = new LinkedList<>();
    }

    // 카드 추가 메서드
    public void addCard(Card card) {
        if (cardQueue.size() >= MAX_CARDS) {
            cardQueue.poll(); // 큐가 꽉 차면 가장 오래된 카드를 제거
        }
        cardQueue.add(card);
    }

    // 현재 카드 사용 후 제거 메서드
    public Card useCard() {
        return cardQueue.poll();
    }

    // 카드 큐 조회 메서드
    public Queue<Card> getCardQueue() {
        return new LinkedList<>(cardQueue); // 큐의 복사본 반환
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
}
