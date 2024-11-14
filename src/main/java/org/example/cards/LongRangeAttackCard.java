package org.example.cards;

import org.example.players.Player;

public class LongRangeAttackCard extends AttackCard {
    public LongRangeAttackCard() {
        super(8, 10);  // 데미지 8, 에너지 소모량 10
    }

    @Override
    protected boolean isInRange(Player user, Player opponent) {
        return opponent.getPositionY() == user.getPositionY() &&
                Math.abs(opponent.getPositionX() - user.getPositionX()) <= 2;
    }

    @Override
    public String getName() {
        return "Long Range Attack";
    }
}
