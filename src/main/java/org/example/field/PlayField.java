package org.example.field;

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

public class PlayField extends JPanel {
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
    private int currentCardIndex1, currentCardIndex2;
    private boolean isCharacter1Turn;

    public PlayField(Character character1, Character character2) {
        this.character1 = character1;
        this.character2 = character2;

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
        }

        playerPixelPosition1 = getWorldPosition(playerGridPosition1);

        playerGridPosition2 = character2.getGridPosition();
        if (playerGridPosition2 == null) {
            playerGridPosition2 = new Point(GRID_COLUMNS - 1, GRID_ROWS - 1);
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

        Card card1 = character1.useCard();
        Card card2 = character2.useCard();

        if (card1 != null && card2 != null) {
            int priority1 = getCardPriority(card1);
            int priority2 = getCardPriority(card2);

            isExecutingAction = true;

            if (priority1 < priority2) {
                executeCardWithDelay(card1, true, () -> {
                    currentCardIndex1++;
                    executeCardWithDelay(card2, false, () -> {
                        currentCardIndex2++;
                        isExecutingAction = false;
                        executeAction(); // 다음 카드 실행
                    });
                });
            } else {
                executeCardWithDelay(card2, false, () -> {
                    currentCardIndex2++;
                    executeCardWithDelay(card1, true, () -> {
                        currentCardIndex1++;
                        isExecutingAction = false;
                        executeAction(); // 다음 카드 실행
                    });
                });
            }
        } else if (card1 != null) {
            // 캐릭터 1만 남은 경우
            isExecutingAction = true;
            executeCardWithDelay(card1, true, () -> {
                currentCardIndex1++;
                isExecutingAction = false;
                executeAction(); // 다음 카드 실행
            });
        } else if (card2 != null) {
            // 캐릭터 2만 남은 경우
            isExecutingAction = true;
            executeCardWithDelay(card2, false, () -> {
                currentCardIndex2++;
                isExecutingAction = false;
                executeAction(); // 다음 카드 실행
            });
        }
    }

    // 카드 실행 후 딜레이를 포함한 메서드
    private void executeCardWithDelay(Card card, boolean isCharacter1, Runnable onComplete) {
        executeCardAction(card, isCharacter1);

        // 동작 + 대기 시간 후 onComplete 실행
        Timer delayTimer = new Timer(4000, e -> {
            ((Timer) e.getSource()).stop();
            onComplete.run(); // 실행 완료 후 콜백 호출
        });
        delayTimer.setRepeats(false);
        delayTimer.start();
    }

    private void executeCardAction(Card card, boolean isCharacter1) {
        CardData cardData = card.getCardData();
        switch (cardData.getCardType()) {
            case ATTACK1, ATTACK2 -> handleAttack(cardData, isCharacter1);
            case MOVE -> handleMove(cardData.getMoveDir(), isCharacter1);
            case DEFENSE -> handleDefense(cardData, isCharacter1);
            case HEAL -> applyState("heal", isCharacter1);
        }
    }

    private void handleAttack(CardData cardData, boolean isCharacter1) {
        Point attackerPosition = isCharacter1 ? playerGridPosition1 : playerGridPosition2;
        Point targetPosition = isCharacter1 ? playerGridPosition2 : playerGridPosition1;

        // 방어 중인지 확인
        boolean targetInDefense = isCharacter1 ? character2.isDefending() : character1.isDefending();

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
        if (attackRange.contains(targetPosition) && !targetInDefense) { // 방어 상태일 경우 hit 무시
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




    private void handleDefense(CardData cardData, boolean isCharacter1) {
        // 방어 상태 적용
        applyState("defense", isCharacter1);

        // 방어 상태 8초 유지
        Timer defenseTimer = new Timer(8000, e -> {
            if (isCharacter1) {
                character1.setDefending(false);
            } else {
                character2.setDefending(false);
            }
            ((Timer) e.getSource()).stop(); // 타이머 종료
        });
        defenseTimer.setRepeats(false);
        defenseTimer.start();
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

            int frameDelay = 33; // 33ms per frame (30 FPS)
            Timer movementTimer = new Timer(frameDelay, new ActionListener() {
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

                        // 이동 애니메이션 소요시간 계산
                        int animationDuration = MOVE_STEPS * frameDelay;
                        int remainingDelay = 4000 - animationDuration; // 4초에서 애니메이션 시간을 뺌

                        // 남은 대기시간 후 다음 카드 실행
                        Timer delayTimer = new Timer(Math.max(remainingDelay, 0), ev -> {
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
            if ("defense".equals(state)) character1.setDefending(true); // 방어 상태 ON
            else if ("hit".equals(state) && character1.isDefending()) return; // 방어 상태 중 hit 방지

            // 상태 처리
            currentImage1 = switch (state) {
                case "attack1" -> character1.getAttack1Image();
                case "attack2" -> character1.getAttack2Image();
                case "heal" -> character1.getHealImage();
                case "defense" -> character1.getDefenseImage();
                case "hit" -> character1.getHitImage();
                default -> playerImage1;
            };
        } else {
            if ("defense".equals(state)) character2.setDefending(true); // 방어 상태 ON
            else if ("hit".equals(state) && character2.isDefending()) return; // 방어 상태 중 hit 방지

            // 상태 처리
            currentImage2 = switch (state) {
                case "attack1" -> character2.getAttack1Image();
                case "attack2" -> character2.getAttack2Image();
                case "heal" -> character2.getHealImage();
                case "defense" -> character2.getDefenseImage();
                case "hit" -> character2.getHitImage();
                default -> playerImage2;
            };
        }
        repaint();

        // 상태 복원 타이머
        if (!"defense".equals(state)) {
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
    }

    private int getCardPriority(Card card) {
        return switch (card.getCardData().getCardType()) {
            case DEFENSE -> 1;
            case MOVE -> 2;
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

        // 캐릭터 카드 큐에 카드 추가
        inuyasha.addCard(new Card(new CardData(CardType.MOVE, 0, 0, "src/main/resources/cards/card2.png", "", false, MoveDirection.RIGHT, null)));
        inuyasha.addCard(new Card(new CardData(CardType.ATTACK1, 10, 5, "src/main/resources/cards/card1.png", "", false, null, AttackShape.HORIZONTAL)));
        inuyasha.addCard(new Card(new CardData(CardType.ATTACK2, 10, 5, "src/main/resources/cards/card1.png", "", false, null, AttackShape.HORIZONTAL)));

        kagome.addCard(new Card(new CardData(CardType.DEFENSE, 0, 3, "src/main/resources/cards/card3.png", "", false, null, null)));
        kagome.addCard(new Card(new CardData(CardType.MOVE, 0, 0, "src/main/resources/cards/card4.png", "", false, MoveDirection.LEFT, null)));
        kagome.addCard(new Card(new CardData(CardType.DEFENSE, 0, 0, "src/main/resources/cards/card4.png", "", false, null, null)));

        PlayField gamePanel = new PlayField(inuyasha, kagome);

        frame.add(gamePanel);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
