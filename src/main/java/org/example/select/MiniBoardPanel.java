package org.example.select;

import javax.swing.*;
import java.awt.*;

public class MiniBoardPanel extends JPanel {
    private final Image playerImage;
    private final Image opponentImage;

    public MiniBoardPanel() {
        setPreferredSize(new Dimension(400, 300)); // 4:3 비율

        // 플레이어와 상대방 캐릭터 이미지를 로드
        playerImage = new ImageIcon("src/main/resources/animations/select/cuteinu.png").getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        opponentImage = new ImageIcon("src/main/resources/animations/select/cutekagome.png").getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 배경 색상
        g.setColor(new Color(245, 245, 220));
        g.fillRect(0, 0, getWidth(), getHeight());

        // 격자 점선 그리기
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.DARK_GRAY);
        g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, new float[]{5, 5}, 0));

        int cellWidth = getWidth() / 4;
        int cellHeight = getHeight() / 3;
        for (int i = 0; i <= 4; i++) g2.drawLine(i * cellWidth, 0, i * cellWidth, getHeight()); // 세로선
        for (int i = 0; i <= 3; i++) g2.drawLine(0, i * cellHeight, getWidth(), i * cellHeight); // 가로선

        // 플레이어 이미지 그리기 (왼쪽 하단 위치)
        int playerX = cellWidth / 4;
        int playerY = (2 * cellHeight) + cellHeight / 4;
        g.drawImage(playerImage, playerX, playerY, null);

        // 상대방 이미지 그리기 (오른쪽 상단 위치)
        int opponentX = (3 * cellWidth) + cellWidth / 4;
        int opponentY = cellHeight / 4;
        g.drawImage(opponentImage, opponentX, opponentY, null);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Mini Board Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.add(new MiniBoardPanel());
        frame.setVisible(true);
    }
}

