package org.example.connect;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import org.example.card.AttackCard;
import org.example.card.AttackShape;
import org.example.card.Card;
import org.example.card.CardData;
import org.example.card.CardType;
import org.example.card.MoveDirection;
import org.example.character.Character;

public class StateManager {
    private final Character player1;
    private final Character player2;
    private final List<int[]> statsList;

    public StateManager(Character player1, Character player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.statsList = new ArrayList<>();
        initializeStatsList();
    }

    private void initializeStatsList() {
        statsList.add(new int[]{
                player1.getHealth(), player1.getStamina(),
                player2.getHealth(), player2.getStamina()
        });

        for (int turn = 0; turn < Math.min(player1.getCardList().size(), player2.getCardList().size()); turn++) {
            Card card1 = player1.getCardList().get(turn);
            Card card2 = player2.getCardList().get(turn);

            processCard(card1, player1, player2);
            processCard(card2, player2, player1);

            statsList.add(new int[]{
                    player1.getHealth(), player1.getStamina(),
                    player2.getHealth(), player2.getStamina()
            });
        }
    }

    private void processCard(Card card, Character player, Character opponent) {
        switch (card.getCardType()) {
            case ATTACK1, ATTACK2 -> {
                if (card instanceof AttackCard attackCard) {
                    handleAttackCard(attackCard, player, opponent);
                } else {
                    throw new IllegalArgumentException("Invalid card type for ATTACK: " + card.getClass().getName());
                }
            }
            case MOVE -> handleMoveCard(card, player);
            case DEFENSE -> player.setDefending(true);
            case HEAL -> handleHealCard(player);
            default -> throw new IllegalArgumentException("Unknown card type: " + card.getCardType());
        }
    }

    private void handleAttackCard(AttackCard attackCard, Character attacker, Character target) {
        Point attackerPosition = attacker.getGridPosition();
        Point targetPosition = target.getGridPosition();

        List<Point> attackRange = attackCard.getGridPosList(attackerPosition);
        boolean isInRange = attackRange.stream().anyMatch(p -> p.equals(targetPosition));

        int staminaCost = attackCard.getCardData().getStamina();
        if (attacker.getStamina() < staminaCost) {
            attacker.consumeStamina(staminaCost);
            System.out.println("Attack failed due to insufficient stamina.");
            return;
        }

        attacker.consumeStamina(staminaCost);

        if (isInRange) {
            int damage = attackCard.getCardData().getDamage();

            if (target.isDefending()) {
                damage = Math.max(0, damage - 15);
            }

            target.takeDamage(damage);
        } else {
            System.out.printf("Attack missed: %s -> %s%n", attacker.getName(), target.getName());
        }
    }

    private void handleMoveCard(Card moveCard, Character player) {
        Point currentPosition = player.getGridPosition();
        MoveDirection direction = moveCard.getCardData().getMoveDir();
        Point newPosition = new Point(currentPosition);

        switch (direction) {
            case UP -> newPosition.translate(0, -1);
            case DOWN -> newPosition.translate(0, 1);
            case LEFT -> newPosition.translate(-1, 0);
            case RIGHT -> newPosition.translate(1, 0);
        }

        if (isValidGridPosition(newPosition)) {
            player.setGridPosition(newPosition);
        } else {
            System.out.printf("Invalid move for %s%n", player.getName());
        }
    }

    private void handleHealCard(Character player) {
        player.restoreStamina(15);
        System.out.printf("Player %s healed stamina by 15. Current stamina: %d%n",
                player.getName(), player.getStamina());
    }

    private boolean isValidGridPosition(Point position) {
        return position.x >= 0 && position.x < 4 && position.y >= 0 && position.y < 3;
    }

    public List<int[]> getStatsList() {
        return statsList;
    }

    public static void main(String[] args) {
        Character inuyasha = new Character(
                "Inuyasha",
                "src/main/resources/animations/cards/이누야샤기본모션200x160.gif",
                "src/main/resources/animations/cards/이누야샤 점프200x160.gif",
                "src/main/resources/animations/characters/이누야샤힐.gif",
                "src/main/resources/animations/characters/이누야샤방어.gif",
                "src/main/resources/animations/cards/이누야샤초가공격200x160.gif",
                "src/main/resources/animations/cards/이누야샤기본공격200x160.gif",
                "src/main/resources/animations/cards/이누야샤 점프200x160.gif",
                new Point(1, 1)
        );

        Character kagome = new Character(
                "Kagome",
                "src/main/resources/animations/cards/이누야샤기본모션200x160.gif",
                "src/main/resources/animations/cards/이누야샤 점프200x160.gif",
                "src/main/resources/animations/characters/이누야샤힐.gif",
                "src/main/resources/animations/characters/이누야샤방어.gif",
                "src/main/resources/animations/cards/이누야샤초가공격200x160.gif",
                "src/main/resources/animations/cards/이누야샤기본공격200x160.gif",
                "src/main/resources/animations/cards/이누야샤 점프200x160.gif",
                new Point(3, 1)
        );

        inuyasha.addCard(new Card(new CardData(CardType.MOVE, 0, 0, "", "", false, MoveDirection.RIGHT, null)));
        inuyasha.addCard(new AttackCard(new CardData(CardType.ATTACK1, 10, 5, "", "", false, null, AttackShape.HORIZONTAL)));
        inuyasha.addCard(new AttackCard(new CardData(CardType.ATTACK2, 15, 7, "", "", false, null, AttackShape.HORIZONTAL)));

        kagome.addCard(new Card(new CardData(CardType.HEAL, 0, 0, "", "", false, null, null)));
        kagome.addCard(new Card(new CardData(CardType.MOVE, 0, 0, "", "", false, MoveDirection.LEFT, null)));
        kagome.addCard(new Card(new CardData(CardType.DEFENSE, 0, 0, "", "", false, null, null)));

        StateManager stateManager = new StateManager(inuyasha, kagome);

        List<int[]> statsList = stateManager.getStatsList();
        for (int i = 0; i < statsList.size(); i++) {
            int[] stats = statsList.get(i);
            System.out.printf("Turn %d: P1[HP=%d, EN=%d], P2[HP=%d, EN=%d]%n",
                    i, stats[0], stats[1], stats[2], stats[3]);
        }
    }
}
