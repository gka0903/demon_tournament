package org.example.test;

import org.example.card.*;
import org.example.character.Character;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class StateManagerTest {
    private final Character player1;
    private final Character player2;
    private final List<int[]> statsList;

    public StateManagerTest(Character player1, Character player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.statsList = new ArrayList<>();
        initializeStatsList();
    }

    private void initializeStatsList() {
        // 초기 상태 저장
        statsList.add(new int[]{
                player1.getHealth(), player1.getStamina(),
                player2.getHealth(), player2.getStamina(),
                player1.getGridPosition().x, player1.getGridPosition().y,
                player2.getGridPosition().x, player2.getGridPosition().y
        });

        // 카드 처리 루프
        for (int turn = 0; turn < Math.min(player1.getCardList().size(), player2.getCardList().size()); turn++) {
            // 방어 상태 초기화
            player1.setDefending(false);
            player2.setDefending(false);

            // 각 턴의 카드 가져오기
            Card card1 = player1.getCardList().get(turn);
            Card card2 = player2.getCardList().get(turn);

            // 카드 우선순위 비교 및 실행
            int priority1 = getCardPriority(card1);
            int priority2 = getCardPriority(card2);

            if (priority1 < priority2) {
                processCard(card1, player1, player2); // Player1의 카드 먼저 실행
                processCard(card2, player2, player1); // Player2의 카드 실행
            } else {
                processCard(card2, player2, player1); // Player2의 카드 먼저 실행
                processCard(card1, player1, player2); // Player1의 카드 실행
            }

            // 턴 종료 후 상태 저장
            statsList.add(new int[]{
                    player1.getHealth(), player1.getStamina(),
                    player2.getHealth(), player2.getStamina(),
                    player1.getGridPosition().x, player1.getGridPosition().y,
                    player2.getGridPosition().x, player2.getGridPosition().y
            });
        }
    }

    private void processCard(Card card, Character player, Character opponent) {
        switch (card.getCardType()) {
            case ATTACK1, ATTACK2 -> {
                if (card instanceof AttackCard attackCard) {
                    handleAttackCard(attackCard, player, opponent);
                }
            }
            case MOVE -> handleMoveCard(card, player);
            case DEFENSE -> handleDefenseCard(player);
            case HEAL -> handleHealCard(player);
            default -> System.out.printf("Unknown card type: %s%n", card.getCardType());
        }
    }

    private void handleAttackCard(AttackCard attackCard, Character attacker, Character target) {
        Point attackerPosition = attacker.getGridPosition();
        Point targetPosition = target.getGridPosition();

        List<Point> attackRange = attackCard.getGridPosList(attackerPosition);
        boolean isInRange = attackRange.contains(targetPosition);

        if (isInRange) {
            int damage = attackCard.getCardData().getDamage();
            int staminaCost = attackCard.getCardData().getStamina();

            if (attacker.getStamina() >= staminaCost) {
                attacker.consumeStamina(staminaCost);
            } else {
                System.out.printf("%s does not have enough stamina to attack!%n", attacker.getName());
                return;
            }

            if (target.isDefending()) {
                damage = Math.max(0, damage - 15); // 방어 중 데미지 감소
            }

            target.takeDamage(damage);
            System.out.printf("%s attacked %s for %d damage.%n", attacker.getName(), target.getName(), damage);
        } else {
            System.out.printf("%s's attack missed.%n", attacker.getName());
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
            System.out.printf("%s moved to (%d, %d).%n", player.getName(), newPosition.x, newPosition.y);
        } else {
            System.out.printf("%s tried to move out of bounds!%n", player.getName());
        }
    }

    private void handleDefenseCard(Character player) {
        player.setDefending(true);
        System.out.printf("%s is now defending.%n", player.getName());
    }

    private void handleHealCard(Character player) {
        player.restoreStamina(15);
        System.out.printf("%s healed stamina by 15. Current stamina: %d%n", player.getName(), player.getStamina());
    }

    private int getCardPriority(Card card) {
        return switch (card.getCardType()) {
            case DEFENSE -> 1;
            case MOVE -> 2;
            case ATTACK1 -> 3;
            case ATTACK2 -> 4;
            case HEAL -> 5;
            default -> 6;
        };
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
                "src/main/resources/animations/cards/이누야샤/이누야샤기본모션200x160.gif",
                "src/main/resources/animations/cards/이누야샤/이누야샤 점프200x160.gif",
                "src/main/resources/animations/characters/이누야샤힐.gif",
                "src/main/resources/animations/characters/이누야샤방어.gif",
                "src/main/resources/animations/cards/이누야샤공격1.gif",
                "src/main/resources/animations/cards/이누야샤공격2.gif",
                "src/main/resources/animations/cards/이누야샤맞는모션.gif",
                new Point(1, 1)
        );

        Character sesshomaru = new Character(
                "Sesshomaru",
                "src/main/resources/animations/cards/셋쇼마루/셋쇼마루기본모션.gif",
                "src/main/resources/animations/cards/셋쇼마루/셋쇼마루점프.gif",
                "src/main/resources/animations/characters/셋쇼마루힐.gif",
                "src/main/resources/animations/characters/셋쇼마루방어.gif",
                "src/main/resources/animations/cards/셋쇼마루공격1.gif",
                "src/main/resources/animations/cards/셋쇼마루공격2.gif",
                "src/main/resources/animations/cards/셋쇼마루맞는모션.gif",
                new Point(3, 1)
        );

        inuyasha.addCard(new AttackCard(new CardData(CardType.ATTACK1, 10, 5, "", "", false, null, AttackShape.HORIZONTAL)));
        inuyasha.addCard(new Card(new CardData(CardType.MOVE, 0, 0, "", "", false, MoveDirection.RIGHT, null)));
        inuyasha.addCard(new Card(new CardData(CardType.DEFENSE, 0, 0, "", "", false, null, null)));

        sesshomaru.addCard(new AttackCard(new CardData(CardType.ATTACK2, 20, 10, "", "", false, null, AttackShape.VERTICAL)));
        sesshomaru.addCard(new Card(new CardData(CardType.MOVE, 0, 0, "", "", false, MoveDirection.LEFT, null)));
        sesshomaru.addCard(new Card(new CardData(CardType.HEAL, 0, 0, "", "", false, null, null)));

        StateManagerTest stateManagerTest = new StateManagerTest(inuyasha, sesshomaru);

        // 결과 출력
        List<int[]> stats = stateManagerTest.getStatsList();
        for (int i = 0; i < stats.size(); i++) {
            int[] state = stats.get(i);
            System.out.printf("Turn %d: P1[HP=%d, EN=%d, Pos=(%d,%d)] P2[HP=%d, EN=%d, Pos=(%d,%d)]%n",
                    i, state[0], state[1], state[4], state[5], state[2], state[3], state[6], state[7]);
        }
    }
}
