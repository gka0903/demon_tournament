package org.example.cards;

import org.example.players.Player;

public abstract class AttackCard implements Card {
    protected int power;       // 공격 데미지
    protected int energyCost;  // 에너지 소모량

    public AttackCard(int power, int energyCost) {
        this.power = power;
        this.energyCost = energyCost;
    }

    // 에너지 소모량을 반환하는 메서드
    public int getEnergyCost() {
        return energyCost;
    }

    @Override
    public void execute(Player user, Player opponent) {
        if (isInRange(user, opponent)) {
            int damage = opponent.isGuarding() ? Math.max(0, power - 15) : power;
            opponent.setHealth(opponent.getHealth() - damage);
            System.out.println(user.getName() + " attacks " + opponent.getName() + " for " + damage + " damage");
            System.out.println(opponent.getName() + " health: " + opponent.getHealth());
        } else {
            System.out.println(user.getName() + " attempts to attack, but " + opponent.getName() + " is out of range.");
        }
    }

    protected abstract boolean isInRange(Player user, Player opponent);
}
