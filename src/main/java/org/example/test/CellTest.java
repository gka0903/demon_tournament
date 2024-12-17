package org.example.test;

import javax.swing.*;

import org.example.field.Cell;

import java.awt.*;

public class CellTest {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Cell Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 400);
            frame.setLayout(new GridLayout(3, 4)); // 5x5 셀 그리드

            // 셀 생성
            Cell[][] cells = new Cell[3][4];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 4; j++) {
                    Cell cell = new Cell();
                    frame.add(cell);
                    cells[i][j] = cell;
                }
            }

            // 테스트: 특정 셀의 색상 변경
            Timer timer = new Timer(1000, e -> cells[2][2].changeColor(Color.RED));
            timer.setRepeats(false);
            timer.start();

            frame.setVisible(true);
        });
    }
}
