package org.example.cards;

import org.example.players.Player;

public class GuardCard implements Card {
    @Override
    public void execute(Player user, Player opponent) {
        user.setGuarding(true);
        System.out.println(user.getName() + " takes a guard stance to reduce incoming damage");
    }

    @Override
    public String getName() {
        return "Guard";
    }
}
