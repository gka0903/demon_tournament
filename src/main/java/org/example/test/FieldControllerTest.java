package org.example.test;

import org.example.field.FieldController;

import javax.swing.*;
import java.awt.*;

public class FieldControllerTest {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Field Controller Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            FieldController fieldController = new FieldController();
            frame.add(fieldController.getFieldPanel());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            // 캐릭터 이동 테스트
            Point startPosition = new Point(0, 1); // 초기 위치
            Point[] positions = {
                    new Point(1, 1), // 오른쪽 이동
                    new Point(2, 1), // 더 오른쪽
                    new Point(3, 1), // 맨 끝
                    new Point(2, 1), // 왼쪽으로 복귀
                    new Point(1, 1), // 더 왼쪽으로
                    new Point(0, 1)  // 초기 위치로 복귀
            };

            Timer timer = new Timer(2000, null); // 1초 간격 실행
            final int[] index = {0}; // 현재 위치 인덱스

            timer.addActionListener(e -> {
                if (index[0] < positions.length) {
                    Point current = (index[0] == 0) ? startPosition : positions[index[0] - 1];
                    Point next = positions[index[0]];

                    // 도착 위치
                    fieldController.changeCellColor(next, Color.GREEN);

                    // 캐릭터 이동
                    fieldController.moveCharacter(next);

                    index[0]++;
                } else {
                    timer.stop();
                }
            });

            timer.start();
        });
    }
}
