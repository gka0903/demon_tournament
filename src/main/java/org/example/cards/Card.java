package org.example.cards;

import org.example.players.Player;

public interface Card {
    void execute(Player user, Player opponent);
    String getName();
}
