package org.example.cards;

import org.example.players.Player;

public class BasicAttackCard extends AttackCard {
    public BasicAttackCard() {
        super(10, 5);  // 기본 데미지 10, 에너지 소모량 5
    }

    @Override
    protected boolean isInRange(Player user, Player opponent) {
        return opponent.getPositionY() == user.getPositionY() &&
                (opponent.getPositionX() == user.getPositionX() ||
                        opponent.getPositionX() == user.getPositionX() - 1 ||
                        opponent.getPositionX() == user.getPositionX() + 1);
    }

    @Override
    public String getName() {
        return "Basic Attack";
    }
}
