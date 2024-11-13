package org.example.cards;

import org.example.players.Player;

public class MoveCards {

    public static class MoveUpCard implements Card {
        @Override
        public void execute(Player user, Player opponent) {
            System.out.println(user.getName() + " attempts to move up.");
            user.move(0, -1);  // 위로 이동
        }

        @Override
        public String getName() {
            return "Move Up";
        }
    }

    public static class MoveDownCard implements Card {
        @Override
        public void execute(Player user, Player opponent) {
            System.out.println(user.getName() + " attempts to move down.");
            user.move(0, 1);  // 아래로 이동
        }

        @Override
        public String getName() {
            return "Move Down";
        }
    }

    public static class MoveLeftCard implements Card {
        @Override
        public void execute(Player user, Player opponent) {
            System.out.println(user.getName() + " attempts to move left.");
            user.move(-1, 0);  // 왼쪽으로 이동
        }

        @Override
        public String getName() {
            return "Move Left";
        }
    }

    public static class MoveRightCard implements Card {
        @Override
        public void execute(Player user, Player opponent) {
            System.out.println(user.getName() + " attempts to move right.");
            user.move(1, 0);  // 오른쪽으로 이동
        }

        @Override
        public String getName() {
            return "Move Right";
        }
    }
}
