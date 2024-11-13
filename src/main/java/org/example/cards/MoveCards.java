package org.example.cards;

import org.example.cards.Card;
import org.example.players.Player;

public class MoveCards {

    public static class MoveUpCard implements Card {
        @Override
        public void execute(Player user, Player opponent) {
            System.out.println(user.getName() + " moves up.");
            user.setPosition(user.getPositionX(), user.getPositionY() - 1);
        }

        @Override
        public String getName() {
            return "Move Up";
        }
    }

    public static class MoveDownCard implements Card {
        @Override
        public void execute(Player user, Player opponent) {
            System.out.println(user.getName() + " moves down.");
            user.setPosition(user.getPositionX(), user.getPositionY() + 1);
        }

        @Override
        public String getName() {
            return "Move Down";
        }
    }

    public static class MoveLeftCard implements Card {
        @Override
        public void execute(Player user, Player opponent) {
            System.out.println(user.getName() + " moves left.");
            user.setPosition(user.getPositionX() - 1, user.getPositionY());
        }

        @Override
        public String getName() {
            return "Move Left";
        }
    }

    public static class MoveRightCard implements Card {
        @Override
        public void execute(Player user, Player opponent) {
            System.out.println(user.getName() + " moves right.");
            user.setPosition(user.getPositionX() + 1, user.getPositionY());
        }

        @Override
        public String getName() {
            return "Move Right";
        }
    }
}
