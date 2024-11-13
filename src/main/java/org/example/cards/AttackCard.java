package org.example.cards;

import org.example.players.Player;

public class AttackCard implements Card {
    private int power;

    public AttackCard(int power) {
        this.power = power;
    }

    @Override
    public void execute(Player user, Player opponent) {
        int damage = opponent.isGuarding() ? power - 15 : power;
        opponent.setHealth(opponent.getHealth() - damage);
        System.out.println(user.getName() + " attacks for " + damage + " damage");
    }

    @Override
    public String getName() {
        return "Attack with power " + power;
    }
}
