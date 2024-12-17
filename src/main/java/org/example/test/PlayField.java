package org.example.test;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.example.character.Character;

public class PlayField extends JPanel implements ActionListener, KeyListener {
    private Timer timer; // 게임 업데이트를 위한 타이머
    private Image playerImage; // 플레이어 이미지를 저장
    private Image movingImage; // 이동 중에 사용할 이미지
    private Image currentImage; // 현재 표시 중인 이미지
    private final int CELL_WIDTH = 200; // 각 칸의 가로 크기
    private final int CELL_HEIGHT = 150; // 각 칸의 세로 크기
    private final int GRID_ROWS = 3; // 그리드 행 개수
    private final int GRID_COLUMNS = 4; // 그리드 열 개수
    private Point playerGridPosition; // 그리드 상에서 플레이어 위치
    private Point playerPixelPosition; // 실제 화면 픽셀 단위 위치
    private Point targetPixelPosition; // 이동할 목표 위치
    private boolean isMoving; // 이동 중인지 여부를 확인하는 플래그
    private boolean[] keys; // 키 입력 상태 배열
    private final int MOVE_STEPS = 30; // 이동 단계 수
    private int currentStep; // 현재 이동 단계
    private Color[][] gridColors; // 셀의 색상을 저장하는 배열
    private Character character1;

    public PlayField(Character character1) {
        keys = new boolean[256];
        isMoving = false;

        // 이미지 로드
        playerImage = character1.getCurrentImage();
        movingImage = character1.getMoveImage();
        currentImage = playerImage; // 초기 이미지는 정지 상태 이미지
        this.character1 = character1;

        // 초기 위치 설정
        playerGridPosition = character1.getGridPosition();
        playerPixelPosition = getWorldPosition(playerGridPosition);
        targetPixelPosition = playerPixelPosition;

        // 그리드 색상 초기화
        gridColors = new Color[GRID_ROWS][GRID_COLUMNS];
        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLUMNS; col++) {
                gridColors[row][col] = Color.LIGHT_GRAY; // 기본 색상 설정
            }
        }

        timer = new Timer(15, this);
        timer.start();

        addKeyListener(this);
        setFocusable(true);
    }

    // 그리드 좌표를 화면상의 중앙 좌표로 변환
    private Point getWorldPosition(Point gridPosition) {
        int x = gridPosition.x * CELL_WIDTH + CELL_WIDTH / 2;
        int y = gridPosition.y * CELL_HEIGHT + CELL_HEIGHT / 2;
        return new Point(x, y);
    }

    // 화면에 그리드와 플레이어 이미지를 그리는 메서드
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 그리드 그리기
        for (int row = 0; row < GRID_ROWS; row++) {
            for (int col = 0; col < GRID_COLUMNS; col++) {
                g.setColor(gridColors[row][col]);
                g.fillRect(col * CELL_WIDTH, row * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);
                g.setColor(Color.BLACK);
                g.drawRect(col * CELL_WIDTH, row * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT);
            }
        }

        // 플레이어 이미지 그리기
        if (currentImage != null) {
            int imageWidth = (int) (CELL_WIDTH ); // 이미지 크기 조정
            int imageHeight = (int) (CELL_HEIGHT); // 이미지 크기 조정
            g.drawImage(
                    currentImage,
                    playerPixelPosition.x - imageWidth / 2,
                    playerPixelPosition.y - imageHeight / 2,
                    imageWidth,
                    imageHeight,
                    this
            );
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (isMoving) {
            moveStep();
        } else {
            handleKeyInput();
        }
    }

    // 이동 단계를 처리
    private void moveStep() {
        // 이동 단계 계산
        int dx = (targetPixelPosition.x - playerPixelPosition.x) / (MOVE_STEPS - currentStep);
        int dy = (targetPixelPosition.y - playerPixelPosition.y) / (MOVE_STEPS - currentStep);

        playerPixelPosition.x += dx;
        playerPixelPosition.y += dy;
        repaint(); // 이미지를 다시 그리기
        currentStep++;

        // 이동 시작 시 이미지 변경
        if (currentStep == 1) {
            currentImage = movingImage; // 이동 중 이미지로 변경
        }

        // 모든 단계가 완료되었으면 이동 종료
        if (currentStep >= MOVE_STEPS) {
            playerPixelPosition = targetPixelPosition;
            isMoving = false;

            // 이동 완료 후 원래 이미지로 복원
            currentImage = playerImage;
            repaint();
        }
    }

    // 키 입력에 따라 이동 처리
    private void handleKeyInput() {
        if (keys[KeyEvent.VK_LEFT] && playerGridPosition.x > 0) {
            movePlayerTo(playerGridPosition.x - 1, playerGridPosition.y);
        } else if (keys[KeyEvent.VK_RIGHT] && playerGridPosition.x < GRID_COLUMNS - 1) {
            movePlayerTo(playerGridPosition.x + 1, playerGridPosition.y);
        } else if (keys[KeyEvent.VK_UP] && playerGridPosition.y > 0) {
            movePlayerTo(playerGridPosition.x, playerGridPosition.y - 1);
        } else if (keys[KeyEvent.VK_DOWN] && playerGridPosition.y < GRID_ROWS - 1) {
            movePlayerTo(playerGridPosition.x, playerGridPosition.y + 1);
        } else if (keys[KeyEvent.VK_H]) { // Heal 상태
            triggerTemporaryState("heal");
        } else if (keys[KeyEvent.VK_S]) { // Defense 상태
            triggerTemporaryState("defense");
        } else if (keys[KeyEvent.VK_A]) { // 공격1 상태
            triggerTemporaryState("attack1");
        } else if (keys[KeyEvent.VK_D]) { // 공격2 상태
            triggerTemporaryState("attack2");
        } else if (keys[KeyEvent.VK_T]) { // 타격 상태 (T 키로 트리거)
            triggerTemporaryState("hit");
        }
    }

    private void triggerTemporaryState(String state) {
        switch (state) {
            case "heal":
                currentImage = character1.getHealImage(); // 힐 이미지로 변경
                break;
            case "defense":
                currentImage = character1.getDefenseImage(); // 방어 이미지로 변경
                break;
            case "attack1":
                currentImage = character1.getAttack1Image(); // 공격1 이미지로 변경
                break;
            case "attack2":
                currentImage = character1.getAttack2Image(); // 공격2 이미지로 변경
                break;
            case "hit":
                currentImage = character1.getHitImage(); // 타격 이미지로 변경
                break;
        }
        repaint();

        // 2초 후 원래 이미지로 복원
        Timer resetTimer = new Timer(2000, e -> {
            currentImage = playerImage; // 원래 이미지로 복원
            repaint();
        });
        resetTimer.setRepeats(false); // 한 번만 실행되도록 설정
        resetTimer.start();
    }

    // 플레이어 이동 처리
    private void movePlayerTo(int newX, int newY) {
        // 이전 셀 색상 초기화
        gridColors[playerGridPosition.y][playerGridPosition.x] = Color.LIGHT_GRAY;

        // 그리드 위치 업데이트
        playerGridPosition.setLocation(newX, newY);
        targetPixelPosition = getWorldPosition(playerGridPosition);

        // 이동 시작
        isMoving = true;
        currentStep = 0;
    }

    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Grid Movement with GIF Swap");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Character inuyasha = new Character(
                "Inuyasha",
                "src/main/resources/animations/cards/이누야샤기본모션200x160.gif",
                "src/main/resources/animations/cards/이누야샤 점프200x160.gif",
                "src/main/resources/animations/characters/이누야샤힐.gif",
                "src/main/resources/animations/characters/이누야샤방어.gif",
                "src/main/resources/animations/cards/이누야샤초가공격200x160.gif",
                "src/main/resources/animations/cards/이누야샤기본공격200x160.gif",
                "src/main/resources/animations/characters/이누야샤방어.gif",
                new Point(0, 0)
        );
        PlayField gamePanel = new PlayField(inuyasha);


        frame.add(gamePanel);

        frame.setSize(1000, 600); // 4:3 비율에 맞춘 필드 크기
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        gamePanel.requestFocusInWindow();
    }
}
