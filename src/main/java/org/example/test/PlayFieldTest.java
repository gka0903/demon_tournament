package org.example.test;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.example.card.AttackShape;
import org.example.card.Card;
import org.example.card.CardData;
import org.example.card.CardType;
import org.example.card.MoveDirection;
import org.example.card.AttackCard;
import org.example.character.Character;

import java.util.List;

public class PlayFieldTest extends JPanel {
    private final int GRID_ROWS = 3;
    private final int GRID_COLUMNS = 4;
    private final int MOVE_STEPS = 15;

    private Image currentImage1, currentImage2;
    private Image movingImage1, movingImage2;
    private Image playerImage1, playerImage2;

    private Point playerGridPosition1, playerGridPosition2;
    private Point playerPixelPosition1, playerPixelPosition2;
    private Color[][] gridColors;

    private Character character1, character2;
    private List<Card> cards1, cards2;
    private int currentCardIndex1, currentCardIndex2;
    private boolean isCharacter1Turn;

    public PlayFieldTest(Character character1, List<Card> cards1, Character character2, List<Card> cards2) {
        this.character1 = character1;
        this.cards1 = cards1;
        this.currentCardIndex1 = 0;

        this.character2 = character2;
        this.cards2 = cards2;
        this.currentCardIndex2 = 0;

        this.isCharacter1Turn = true;

        playerImage1 = character1.getCurrentImage();
        movingImage1 = character1.getMoveImage();
        currentImage1 = playerImage1;

        playerImage2 = character2.getCurrentImage();
        movingImage2 = character2.getMoveImage();
        currentImage2 = playerImage2;

        // 초기 위치 설정
        playerGridPosition1 = character1.getGridPosition();
        if (playerGridPosition1 == null) {
            // 기본 위치를 (0, 0)으로 설정
            playerGridPosition1 = new Point(0, 0);
            System.err.println("Warning: Character1 grid position was null. Set to default (0, 0).");
        }

        playerPixelPosition1 = getWorldPosition(playerGridPosition1);

        playerGridPosition2 = character2.getGridPosition();
        if (playerGridPosition2 == null) {
            // 기본 위치를 오른쪽 하단 끝으로 설정
            playerGridPosition2 = new Point(GRID_COLUMNS - 1, GRID_ROWS - 1);
            System.err.println("Warning: Character2 grid position was null. Set to default to bottom-right.");
        }

        playerPixelPosition2 = getWorldPosition(playerGridPosition2);

        gridColors = new Color[GRID_ROWS][GRID_COLUMNS];
        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLUMNS; col++) {
                gridColors[row][col] = Color.LIGHT_GRAY;
            }
        }

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                playerPixelPosition1 = getWorldPosition(playerGridPosition1);
                playerPixelPosition2 = getWorldPosition(playerGridPosition2);
                repaint();
            }
        });

        // 초기 1초 대기 후 실행
        Timer initialDelayTimer = new Timer(1000, e -> {
            ((Timer) e.getSource()).stop();
            executeAction(); // 1초 딜레이 후 액션 실행 시작
        });
        initialDelayTimer.setRepeats(false);
        initialDelayTimer.start();

//        executeAction();
    }

    private Point getWorldPosition(Point gridPosition) {
        int cellWidth = getWidth() / GRID_COLUMNS;
        int cellHeight = getHeight() / GRID_ROWS;
        int x = gridPosition.x * cellWidth + cellWidth / 2;
        int y = gridPosition.y * cellHeight + cellHeight / 2;
        return new Point(x, y);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int cellWidth = getWidth() / GRID_COLUMNS;
        int cellHeight = getHeight() / GRID_ROWS;

        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLUMNS; col++) {
                g.setColor(gridColors[row][col]);
                g.fillRect(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
                g.setColor(Color.BLACK);
                g.drawRect(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
            }
        }

        if (currentImage1 != null) {
            g.drawImage(
                    currentImage1,
                    playerPixelPosition1.x - cellWidth / 2,
                    playerPixelPosition1.y - cellHeight / 2,
                    cellWidth,
                    cellHeight,
                    this
            );
        }

        if (currentImage2 != null) {
            g.drawImage(
                    currentImage2,
                    playerPixelPosition2.x - cellWidth / 2,
                    playerPixelPosition2.y - cellHeight / 2,
                    cellWidth,
                    cellHeight,
                    this
            );
        }
    }

    private boolean isExecutingAction = false; // 실행 중 상태 플래그

    private void executeAction() {
        if (isExecutingAction) return; // 실행 중이면 무시

        if (currentCardIndex1 < cards1.size() || currentCardIndex2 < cards2.size()) {
            isExecutingAction = true; // 실행 중 플래그 설정

            Card card1 = currentCardIndex1 < cards1.size() ? cards1.get(currentCardIndex1) : null;
            Card card2 = currentCardIndex2 < cards2.size() ? cards2.get(currentCardIndex2) : null;

            if (card1 != null && card2 != null) {
                int priority1 = getCardPriority(card1);
                int priority2 = getCardPriority(card2);

                if (priority1 < priority2) {
                    executeCardWithDelay(card1, true, () -> {
                        currentCardIndex1++;
                        isExecutingAction = false; // 실행 완료 후 플래그 해제
                        executeAction(); // 다음 액션 실행
                    });
                } else if (priority1 > priority2) {
                    executeCardWithDelay(card2, false, () -> {
                        currentCardIndex2++;
                        isExecutingAction = false; // 실행 완료 후 플래그 해제
                        executeAction(); // 다음 액션 실행
                    });
                } else { // 우선순위가 같을 경우
                    executeCardWithDelay(card1, true, () -> {
                        currentCardIndex1++;
                        executeCardWithDelay(card2, false, () -> {
                            currentCardIndex2++;
                            isExecutingAction = false; // 실행 완료 후 플래그 해제
                            executeAction(); // 다음 액션 실행
                        });
                    });
                }
            } else if (card1 != null) {
                executeCardWithDelay(card1, true, () -> {
                    currentCardIndex1++;
                    isExecutingAction = false; // 실행 완료 후 플래그 해제
                    executeAction(); // 다음 액션 실행
                });
            } else if (card2 != null) {
                executeCardWithDelay(card2, false, () -> {
                    currentCardIndex2++;
                    isExecutingAction = false; // 실행 완료 후 플래그 해제
                    executeAction(); // 다음 액션 실행
                });
            }
        }
    }

    // 카드 실행 후 딜레이를 포함한 메서드
    private void executeCardWithDelay(Card card, boolean isCharacter1, Runnable onComplete) {
        executeCardAction(card, isCharacter1);

        // 동작 + 대기 시간 4초 후 onComplete 실행
        Timer delayTimer = new Timer(4000, e -> {
            ((Timer) e.getSource()).stop();
            onComplete.run(); // 실행 완료 후 콜백
        });
        delayTimer.setRepeats(false);
        delayTimer.start();
    }

    private void executeCardWithDelay(Card card, boolean isCharacter1) {
        executeCardAction(card, isCharacter1);

        // 추가적인 동작 후 4초 간 지연을 포함 (예: 애니메이션이나 상태 복원)
        Timer delayTimer = new Timer(4000, e -> {
            // 다음 동작까지의 대기 타이머, 필요 시 추가 로직 삽입 가능
            ((Timer) e.getSource()).stop();
        });
        delayTimer.setRepeats(false);
        delayTimer.start();
    }

    private void executeCardAction(Card card, boolean isCharacter1) {
        CardData cardData = card.getCardData();
        switch (cardData.getCardType()) {
            case ATTACK1, ATTACK2 -> handleAttack(cardData, isCharacter1);
            case MOVE -> handleMove(cardData.getMoveDir(), isCharacter1);
            case DEFENSE -> applyState("defense", isCharacter1);
            case HEAL -> applyState("heal", isCharacter1);
        }
    }

    private void handleAttack(CardData cardData, boolean isCharacter1) {
        Point attackerPosition = isCharacter1 ? playerGridPosition1 : playerGridPosition2;
        Point targetPosition = isCharacter1 ? playerGridPosition2 : playerGridPosition1;

        // 공격 동작에 따라 캐릭터 모션 적용
        String attackState = cardData.getCardType() == CardType.ATTACK1 ? "attack1" : "attack2";
        applyState(attackState, isCharacter1);

        AttackCard attackCard = new AttackCard(cardData);
        List<Point> attackRange = attackCard.getGridPosList(attackerPosition);

        // 공격 범위 타일 색상 변경
        for (Point p : attackRange) {
            if (isValidGridPosition(p)) {
                gridColors[p.y][p.x] = Color.RED; // 공격 범위를 빨간색으로 설정
            }
        }
        repaint();

        // 0.5초 후 히트 모션 적용
        if (attackRange.contains(targetPosition)) {
            Timer hitDelayTimer = new Timer(500, e -> {
                if (isCharacter1) {
                    applyState("hit", false); // Character 2 맞음
                } else {
                    applyState("hit", true);  // Character 1 맞음
                }
                ((Timer) e.getSource()).stop(); // 타이머 종료
            });
            hitDelayTimer.setRepeats(false);
            hitDelayTimer.start();
        }

        // 1초 후 타일 색상 초기화
        Timer clearTilesTimer = new Timer(1000, e -> {
            clearAttackTiles(attackRange);
            repaint();
            ((Timer) e.getSource()).stop();
        });
        clearTilesTimer.setRepeats(false);
        clearTilesTimer.start();

        // 다음 액션 실행을 위한 딜레이
        Timer delayTimer = new Timer(4000, e -> {
            executeAction();
            ((Timer) e.getSource()).stop();
        });
        delayTimer.setRepeats(false);
        delayTimer.start();
    }

    private void handleMove(MoveDirection direction, boolean isCharacter1) {
        Point gridPosition = isCharacter1 ? playerGridPosition1 : playerGridPosition2;
        Point pixelPosition = isCharacter1 ? playerPixelPosition1 : playerPixelPosition2;

        int dx = 0, dy = 0;
        switch (direction) {
            case UP -> dy = -1;
            case DOWN -> dy = 1;
            case LEFT -> dx = -1;
            case RIGHT -> dx = 1;
        }

        int newX = gridPosition.x + dx;
        int newY = gridPosition.y + dy;

        if (newX >= 0 && newX < GRID_COLUMNS && newY >= 0 && newY < GRID_ROWS) {
            Point startPosition = new Point(pixelPosition);
            Point endPosition = getWorldPosition(new Point(newX, newY));
            gridPosition.setLocation(newX, newY);

            if (isCharacter1) {
                currentImage1 = movingImage1;
            } else {
                currentImage2 = movingImage2;
            }

            Timer movementTimer = new Timer(33, new ActionListener() {
                int steps = 0;

                @Override
                public void actionPerformed(ActionEvent e) {
                    int dx = (endPosition.x - startPosition.x) / MOVE_STEPS;
                    int dy = (endPosition.y - startPosition.y) / MOVE_STEPS;

                    pixelPosition.x += dx;
                    pixelPosition.y += dy;
                    repaint();
                    steps++;

                    if (steps >= MOVE_STEPS) {
                        pixelPosition.setLocation(endPosition);
                        if (isCharacter1) {
                            currentImage1 = playerImage1;
                        } else {
                            currentImage2 = playerImage2;
                        }
                        repaint();
                        ((Timer) e.getSource()).stop();

                        int remainingDelay = 4000 - (MOVE_STEPS * 33);
                        Timer delayTimer = new Timer(remainingDelay, ev -> {
                            executeAction();
                            ((Timer) ev.getSource()).stop();
                        });
                        delayTimer.setRepeats(false);
                        delayTimer.start();
                    }
                }
            });

            movementTimer.start();
        }
    }

    private void applyState(String state, boolean isCharacter1) {
        if (isCharacter1) {
            System.out.println("Character 1 state: " + state); // 디버그 로그
            switch (state) {
                case "attack1" -> currentImage1 = character1.getAttack1Image();
                case "attack2" -> currentImage1 = character1.getAttack2Image();
                case "heal" -> currentImage1 = character1.getHealImage();
                case "defense" -> currentImage1 = character1.getDefenseImage();
                case "hit" -> currentImage1 = character1.getHitImage();
            }
        } else {
            System.out.println("Character 2 state: " + state); // 디버그 로그
            switch (state) {
                case "attack1" -> currentImage2 = character2.getAttack1Image();
                case "attack2" -> currentImage2 = character2.getAttack2Image();
                case "heal" -> currentImage2 = character2.getHealImage();
                case "defense" -> currentImage2 = character2.getDefenseImage();
                case "hit" -> currentImage2 = character2.getHitImage();
            }
        }
        repaint();

        Timer stateRestoreTimer = new Timer(3000, e -> {
            if (isCharacter1) {
                currentImage1 = playerImage1;
            } else {
                currentImage2 = playerImage2;
            }
            repaint();
            executeAction(); // 다음 동작 실행
            ((Timer) e.getSource()).stop();
        });
        stateRestoreTimer.setRepeats(false);
        stateRestoreTimer.start();
    }

    private int getCardPriority(Card card) {
        return switch (card.getCardData().getCardType()) {
            case MOVE -> 1;
            case DEFENSE -> 2;
            case ATTACK1 -> 3;
            case ATTACK2 -> 4; // ATTACK2 우선순위 명시
            case HEAL -> 5;
            default -> 6;
        };
    }

    private boolean isValidGridPosition(Point p) {
        return p.x >= 0 && p.x < GRID_COLUMNS && p.y >= 0 && p.y < GRID_ROWS;
    }

    private void clearAttackTiles(List<Point> attackRange) {
        for (Point p : attackRange) {
            if (isValidGridPosition(p)) {
                gridColors[p.y][p.x] = Color.LIGHT_GRAY; // 기본 색상으로 초기화
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("PlayField Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Character inuyasha = new Character(
                "Inuyasha",
                "src/main/resources/animations/cards/이누야샤기본모션200x160.gif",
                "src/main/resources/animations/cards/이누야샤 점프200x160.gif",
                "src/main/resources/animations/characters/이누야샤힐.gif",
                "src/main/resources/animations/characters/이누야샤방어.gif",
                "src/main/resources/animations/cards/이누야샤초가공격200x160.gif",
                "src/main/resources/animations/cards/이누야샤기본공격200x160.gif",
                "src/main/resources/animations/cards/이누야샤 점프200x160.gif",
                new Point(0, 1)
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

        List<Card> cards1 = List.of(
                new Card(new CardData(CardType.MOVE, 0, 0, "src/main/resources/cards/card2.png", "", false, MoveDirection.RIGHT, null)),
                new Card(new CardData(CardType.ATTACK1, 10, 5, "src/main/resources/cards/card1.png", "", false, null, AttackShape.HORIZONTAL)),
                new Card(new CardData(CardType.ATTACK2, 10, 5, "src/main/resources/cards/card1.png", "", false, null, AttackShape.T_UP))
        );

        List<Card> cards2 = List.of(
                new Card(new CardData(CardType.DEFENSE, 0, 3, "src/main/resources/cards/card3.png", "", false, null, null)),
                new Card(new CardData(CardType.MOVE, 0, 0, "src/main/resources/cards/card4.png", "", false, MoveDirection.LEFT, null)),
                new Card(new CardData(CardType.HEAL, 0, 0, "src/main/resources/cards/card4.png", "", false, null, null))
        );

        PlayFieldTest gamePanel = new PlayFieldTest(inuyasha, cards1, kagome, cards2);

        frame.add(gamePanel);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
