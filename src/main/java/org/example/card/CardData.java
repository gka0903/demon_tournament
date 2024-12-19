package org.example.card;

public class CardData {
    // 기본 정보
    private CardType cardType;        // 카드 타입
    private int damage;               // 피해량
    private int stamina;              // 스태미나 소비량
    private String cardSpritePath;    // 카드 앞면 이미지 경로
    private String cardBackSpritePath; // 카드 뒷면 이미지 경로
    private boolean isUsed;           // 카드 사용 여부

    // MoveCard 관련
    private MoveDirection moveDir;    // 이동 방향

    // AttackCard 관련
    private AttackShape atkShape;     // 공격 형태

    // 생성자
    public CardData(CardType cardType, int damage, int stamina, String cardSpritePath,
                    String cardBackSpritePath, boolean isUsed, MoveDirection moveDir, AttackShape atkShape) {
        this.cardType = cardType;
        this.damage = clamp(damage, 0, 99);
        this.stamina = clamp(stamina, 0, 99);
        this.cardSpritePath = cardSpritePath; // 경로만 저장
        this.cardBackSpritePath = cardBackSpritePath;
        this.isUsed = isUsed;
        this.moveDir = moveDir;
        this.atkShape = atkShape;
    }

    // 클램프 메서드 (Mathf.Clamp 대체)
    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }

    // Getter/Setter
    public CardType getCardType() {
        return cardType;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = clamp(damage, 0, 99);
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        this.stamina = clamp(stamina, 0, 99);
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public String getCardSpritePath() {
        return cardSpritePath;
    }

    public void setCardSpritePath(String cardSpritePath) {
        this.cardSpritePath = cardSpritePath;
    }

    public String getCardBackSpritePath() {
        return cardBackSpritePath;
    }

    public void setCardBackSpritePath(String cardBackSpritePath) {
        this.cardBackSpritePath = cardBackSpritePath;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public MoveDirection getMoveDir() {
        return moveDir;
    }

    public void setMoveDir(MoveDirection moveDir) {
        this.moveDir = moveDir;
    }

    public AttackShape getAtkShape() {
        return atkShape;
    }

    public void setAtkShape(AttackShape atkShape) {
        this.atkShape = atkShape;
    }
}
