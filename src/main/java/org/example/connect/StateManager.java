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
        // 초기 상태 추가
        statsList.add(new int[]{
                player1.getHealth(), player1.getStamina(),
                player2.getHealth(), player2.getStamina()
        });

        // 카드 처리 루프
        for (int turn = 0; turn < Math.min(player1.getCardList().size(), player2.getCardList().size()); turn++) {
            Card card1 = player1.getCardList().get(turn);
            Card card2 = player2.getCardList().get(turn);

            processCard(card1, player1, player2);
            processCard(card2, player2, player1);

            // 현재 상태를 추가
            statsList.add(new int[]{
                    player1.getHealth(), player1.getStamina(),
                    player2.getHealth(), player2.getStamina()
            });
        }
    }

    private void processCard(Card card, Character player, Character opponent) {
        switch (card.getCardType()) {
            case ATTACK1, ATTACK2 -> {
                // 카드가 AttackCard인 경우에만 처리하도록 수정
                if (card instanceof AttackCard attackCard) {
                    handleAttackCard(attackCard, player, opponent); // AttackCard로 처리
                } else {
                    System.err.printf("Invalid card type for ATTACK: %s%n", card.getClass().getName()); // 타입 오류 처리
                }
            }
            case MOVE -> handleMoveCard(card, player);  // MOVE 카드 처리
            case DEFENSE -> player.setDefending(true);  // DEFENSE 카드 처리
            case HEAL -> handleHealCard(player);  // HEAL 카드 처리
            default -> System.out.println("Unknown card type: " + card.getCardType());  // 알 수 없는 카드 타입 처리
        }
    }

    private void handleAttackCard(AttackCard attackCard, Character attacker, Character target) {
        Point attackerPosition = attacker.getGridPosition();
        Point targetPosition = target.getGridPosition();

        List<Point> attackRange = attackCard.getGridPosList(attackerPosition);
        boolean isInRange = attackRange.stream().anyMatch(p -> p.equals(targetPosition));

        if (isInRange) {
            int damage = attackCard.getCardData().getDamage();  // 카드에서 피해량 가져오기
            int staminaCost = attackCard.getCardData().getStamina();  // 카드에서 스태미나 소비량 가져오기

            // 스태미나 소비 처리
            if (attacker.getStamina() >= staminaCost) {
                attacker.consumeStamina(staminaCost);  // 스태미나 차감
            } else {
                System.out.println("Not enough stamina to perform the attack!");
                return;  // 스태미나가 부족하면 공격하지 않음
            }

            // 방어 중이면 데미지 감소
            if (target.isDefending()) {
                damage = Math.max(0, damage - 15);  // 방어 시 데미지 감소
            }

            target.takeDamage(damage);  // 대상에게 피해를 입힘
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
