package org.example.field;

import javax.swing.*;
import java.awt.*;

public class Cell extends JPanel {
    private Color originalColor;

    public Cell() {
        // 초기 설정: 셀 배경색과 경계선
        originalColor = getBackground();
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    // 색 변경 메서드
    public void changeColor(Color color) {
        Color newColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 50); // 투명도 20% 설정
        new Thread(() -> flashColor(newColor)).start(); // 깜빡임 효과를 새 스레드에서 실행
    }

    // 깜빡이는 색상 효과
    private void flashColor(Color color) {
        try {
            for (int i = 0; i < 3; i++) {
                SwingUtilities.invokeLater(() -> setBackground(color)); // 색상 변경
                Thread.sleep(200); // 0.2초 대기
                SwingUtilities.invokeLater(() -> setBackground(originalColor)); // 원래 색으로 복원
                Thread.sleep(200); // 0.2초 대기
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
