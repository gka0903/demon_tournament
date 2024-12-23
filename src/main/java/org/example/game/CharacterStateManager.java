package org.example.game;

import org.example.card.*;
import org.example.character.Character;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;


public class CharacterStateManager {
    private final Character character1;
    private final Character character2;
    private final List<int[]> stateHistory; // 각 턴마다 HP와 EN 상태 저장

    public CharacterStateManager(Character character1, Character character2) {
        this.character1 = character1;
        this.character2 = character2;
        this.stateHistory = new ArrayList<>();
        initializeState();
    }

    private void initializeState() {
        // 초기 상태 저장
        stateHistory.add(new int[]{
                character1.getHealth(), character1.getStamina(),
                character2.getHealth(), character2.getStamina()
        });
    }

    public void processCards() {
        List<Card> cards1 = character1.getCardList();
        List<Card> cards2 = character2.getCardList();

        int maxTurns = Math.max(cards1.size(), cards2.size());

        for (int turn = 0; turn < maxTurns; turn++) {
            Card card1 = turn < cards1.size() ? cards1.get(turn) : null;
            Card card2 = turn < cards2.size() ? cards2.get(turn) : null;

            if (card1 != null) {
                processCard(card1, character1, character2);
            }

            if (card2 != null) {
                processCard(card2, character2, character1);
            }

            // 턴 종료 후 상태 저장
            stateHistory.add(new int[]{
                    character1.getHealth(), character1.getStamina(),
                    character2.getHealth(), character2.getStamina()
            });
        }
    }

    private void processCard(Card card, Character player, Character opponent) {
        switch (card.getCardType()) {
            case ATTACK1, ATTACK2 -> handleAttack(card, player, opponent);
            case MOVE -> handleMove(card, player);
            case DEFENSE -> player.setDefending(true);
            case HEAL -> handleHeal(player);
        }
    }

    private void handleAttack(Card card, Character attacker, Character target) {
        if (!(card instanceof AttackCard attackCard)) return;

        Point attackerPosition = attacker.getGridPosition();
        Point targetPosition = target.getGridPosition();

        List<Point> attackRange = attackCard.getGridPosList(attackerPosition);
        boolean isInRange = attackRange.stream().anyMatch(p -> p.equals(targetPosition));

        int damage = attackCard.getCardData().getDamage();
        int staminaCost = attackCard.getCardData().getStamina();

        if (attacker.getStamina() >= staminaCost) {
            attacker.consumeStamina(staminaCost);
        } else {
            return; // 스태미나 부족으로 공격 불가
        }

        if (isInRange) {
            if (target.isDefending()) {
                damage = Math.max(0, damage - 10); // 방어 시 데미지 감소
            }
            target.takeDamage(damage);

            // 체력 확인
            if (isGameOver()) {
                showGameOverMessage();
                return; // 게임 종료
            }
        }
    }

    private boolean isGameOver() {
        return character1.getHealth() <= 0 || character2.getHealth() <= 0;
    }

    private void showGameOverMessage() {
        String winner = character1.getHealth() > 0 ? "Character 1 Wins!" : "Character 2 Wins!";
        JOptionPane.showMessageDialog(null, "GAME OVER!\n" + winner, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0); // 게임 종료
    }

    private void handleMove(Card card, Character player) {
        Point currentPosition = player.getGridPosition();
        MoveDirection direction = card.getCardData().getMoveDir();
        Point newPosition = new Point(currentPosition);

        switch (direction) {
            case UP -> newPosition.translate(0, -1);
            case DOWN -> newPosition.translate(0, 1);
            case LEFT -> newPosition.translate(-1, 0);
            case RIGHT -> newPosition.translate(1, 0);
        }

        if (isValidGridPosition(newPosition)) {
            player.setGridPosition(newPosition);
        }
    }

    private void handleHeal(Character player) {
        player.restoreStamina(15);
    }

    private boolean isValidGridPosition(Point position) {
        return position.x >= 0 && position.x < 4 && position.y >= 0 && position.y < 3;
    }

    public List<int[]> getStateHistory() {
        return stateHistory;
    }

    public Point[] getFinalPositions() {
        return new Point[]{
                character1.getGridPosition(),
                character2.getGridPosition()
        };
    }

    public static void main(String[] args) {
        Character inuyasha = new Character(
                "Inuyasha",
                "src/main/resources/animations/cards/이누야샤/이누야샤기본모션200x160.gif",
                "src/main/resources/animations/cards/이누야샤/이누야샤 점프200x160.gif",
                "src/main/resources/animations/cards/이누야샤/이누야샤힐200x160.gif",
                "src/main/resources/animations/cards/이누야샤/이누야샤방어200x160.gif",
                "src/main/resources/animations/cards/이누야샤/이누야샤기본공격200x160.gif",
                "src/main/resources/animations/cards/이누야샤/이누야샤추가공격200x160.gif",
                "src/main/resources/animations/cards/이누야샤/이누야샤맞는모션200x160.gif",
                new Point(0, 1)
        );

        Character sesshomaru = new Character(
                "Sesshomaru",
                "src/main/resources/animations/cards/셋쇼마루/셋쇼제자리.gif",
                "src/main/resources/animations/cards/셋쇼마루/셋쇼점프.gif",
                "src/main/resources/animations/cards/셋쇼마루/셋쇼힐.gif",
                "src/main/resources/animations/cards/셋쇼마루/셋쇼방어.gif",
                "src/main/resources/animations/cards/셋쇼마루/셋쇼공격.gif",
                "src/main/resources/animations/cards/셋쇼마루/셋쇼추가공격.gif",
                "src/main/resources/animations/cards/셋쇼마루/셋쇼맞음.gif",
                new Point(3, 1)
        );

        inuyasha.addCard(new AttackCard(new CardData(CardType.ATTACK1, 20, 5, "", "", false, null, AttackShape.HORIZONTAL)));
        inuyasha.addCard(new Card(new CardData(CardType.HEAL, 0, 0, "", "", false, null, null)));

        sesshomaru.addCard(new Card(new CardData(CardType.DEFENSE, 0, 3, "", "", false, null, null)));
        sesshomaru.addCard(new Card(new CardData(CardType.MOVE, 0, 0, "", "", false, MoveDirection.LEFT, null)));

        CharacterStateManager manager = new CharacterStateManager(inuyasha, sesshomaru);
        manager.processCards();

        List<int[]> history = manager.getStateHistory();
        for (int i = 0; i < history.size(); i++) {
            int[] state = history.get(i);
            System.out.printf("Turn %d: Inuyasha [HP=%d, EN=%d], Sesshomaru [HP=%d, EN=%d]%n",
                    i, state[0], state[1], state[2], state[3]);
        }
    }
}
