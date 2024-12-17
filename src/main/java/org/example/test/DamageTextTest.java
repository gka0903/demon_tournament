package org.example.test;

import javax.swing.*;
import java.awt.*;
import org.example.ui.DamageText;

public class DamageTextTest {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Damage Text Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 400);
            frame.setLayout(null);

            JPanel panel = new JPanel();
            panel.setLayout(null);
            panel.setBounds(0, 0, 400, 400);
            frame.add(panel);

            // 클릭 시 데미지 텍스트 생성
            panel.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    Point clickPoint = e.getPoint();
                    DamageText.createDamageText(50, clickPoint, panel);
                }
            });

            frame.setVisible(true);
        });
    }
}
