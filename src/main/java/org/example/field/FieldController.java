package org.example.field;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class FieldController {
    private JPanel fieldPanel; // 필드 전체를 나타내는 패널
    private Cell[][] cells; // 셀 배열
    private JLabel playerCharacter; // 플레이어 캐릭터
    private int rowsCount = 3; // 행 개수
    private int columnsCount = 4; // 열 개수
    private int cellSize = 100; // 셀 크기 (픽셀 단위)

    public FieldController() {
        initializeField(); // 필드 초기화
    }

    // 필드 초기화
    private void initializeField() {
        fieldPanel = new JPanel(null); // 절대 레이아웃
        fieldPanel.setPreferredSize(new Dimension(columnsCount * cellSize, rowsCount * cellSize));
        fieldPanel.setBackground(Color.LIGHT_GRAY);

        cells = new Cell[columnsCount][rowsCount];

        // 셀 초기화 및 추가
        for (int row = 0; row < rowsCount; row++) {
            for (int col = 0; col < columnsCount; col++) {
                Cell cell = new Cell();
                cell.setBounds(col * cellSize, row * cellSize, cellSize, cellSize);
                cells[col][row] = cell;
                fieldPanel.add(cell); // 셀을 패널에 추가
            }
        }

        // 초기 캐릭터 추가
        playerCharacter = createCharacterLabel("Player", Color.BLUE);
        fieldPanel.add(playerCharacter);
        moveCharacter(new Point(0, 1)); // 초기 위치
    }

    // 캐릭터 이동 메서드
    public void moveCharacter(Point newGridPosition) {
        Point targetPos = getWorldPos(newGridPosition);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int steps = 20; // 총 이동 단계 수
            int stepCount = 0;
            int dx = (targetPos.x - playerCharacter.getX()) / steps;
            int dy = (targetPos.y - playerCharacter.getY()) / steps;

            @Override
            public void run() {
                if (stepCount >= steps) {
                    timer.cancel(); // 이동 완료 후 타이머 중지
                    playerCharacter.setLocation(targetPos);
                    return;
                }

                // 현재 위치를 단계적으로 업데이트
                SwingUtilities.invokeLater(() -> playerCharacter.setLocation(
                        playerCharacter.getX() + dx,
                        playerCharacter.getY() + dy
                ));

                stepCount++;
            }
        }, 0, 20); // 20ms 간격으로 실행
    }

    // 셀의 월드 좌표를 반환
    private Point getWorldPos(Point gridPosition) {
        int x = gridPosition.x * cellSize;
        int y = gridPosition.y * cellSize;
        return new Point(x, y);
    }

    // 셀 색상 변경
    public void changeCellColor(Point gridPosition, Color color) {
        Cell targetCell = cells[gridPosition.x][gridPosition.y];
        targetCell.changeColor(color);
    }

    // 필드 패널 반환 (UI 구성용)
    public JPanel getFieldPanel() {
        return fieldPanel;
    }

    // 캐릭터 생성 메서드
    private JLabel createCharacterLabel(String name, Color color) {
        JLabel label = new JLabel(name, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(color);
        label.setForeground(Color.WHITE);
        label.setBounds(0, 0, cellSize, cellSize); // 크기 설정
        return label;
    }
}
