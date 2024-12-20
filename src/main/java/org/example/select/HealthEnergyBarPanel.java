package org.example.select;

import javax.swing.*;
import java.awt.*;

public class HealthEnergyBarPanel extends JPanel {
    private int playerDM = 100, playerEN = 100, opponentDM = 100, opponentEN = 100;
    private final int maxDM = 100, maxEN = 100;
    private final Image playerImage, opponentImage;

    public HealthEnergyBarPanel() {
        playerImage = new ImageIcon("src/main/resources/animations/select/inuyasha.png").getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        opponentImage = new ImageIcon("src/main/resources/animations/select/kagome.png").getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);

        JPanel buttonPanel = new JPanel();
        addButton(buttonPanel, "-10 HP", () -> updateValue(() -> playerDM = Math.max(playerDM - 10, 0)));
        addButton(buttonPanel, "+10 HP", () -> updateValue(() -> playerDM = Math.min(playerDM + 10, maxDM)));
        addButton(buttonPanel, "-10 Energy", () -> updateValue(() -> playerEN = Math.max(playerEN - 10, 0)));
        addButton(buttonPanel, "+10 Energy", () -> updateValue(() -> playerEN = Math.min(playerEN + 10, maxEN)));
        this.setLayout(new BorderLayout());
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addButton(JPanel panel, String label, Runnable action) {
        JButton button = new JButton(label);
        button.addActionListener(e -> action.run());
        panel.add(button);
    }

    private void updateValue(Runnable updater) {
        updater.run();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        drawCharacterImage(g, playerImage, 10, 30);
        drawCharacterImage(g, opponentImage, 710, 30);

        drawBar(g, "DM: " + playerDM + "/" + maxDM, playerDM, maxDM, 100, 30, Color.RED);
        drawBar(g, "EN: " + playerEN + "/" + maxEN, playerEN, maxEN, 100, 60, Color.YELLOW);
        drawBar(g, "DM: " + opponentDM + "/" + maxDM, opponentDM, maxDM, 400, 30, Color.RED);
        drawBar(g, "EN: " + opponentEN + "/" + maxEN, opponentEN, maxEN, 400, 60, Color.YELLOW);
    }

    private void drawCharacterImage(Graphics g, Image img, int x, int y) {
        g.drawImage(img, x, y, null);
    }

    private void drawBar(Graphics g, String label, int value, int max, int x, int y, Color color) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(x, y, 300, 20);
        g.setColor(color);
        int barWidth = (int) (300 * (value / (double) max));
        g.fillRect(x + (300 - barWidth), y, barWidth, 20);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, 300, 20);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString(label, x + 110, y + 15);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Health and Energy Bar Panel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 200);
        frame.add(new HealthEnergyBarPanel());
        frame.setVisible(true);
    }
}

