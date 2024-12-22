package org.example.select;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HealthEnergyBarPanel extends JPanel {
    private int playerDM = 100, playerEN = 100, opponentDM = 100, opponentEN = 100;
    private final int maxDM = 100, maxEN = 100;
    private final Image playerImage, opponentImage;

    public HealthEnergyBarPanel(List<int[]> statsList) {
        // Load images using class loader
        playerImage = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/animations/select/inuyasha.png"));
        opponentImage = Toolkit.getDefaultToolkit().createImage(getClass().getResource("/animations/select/maru.png"));

        // Convert statsList to mutable list
        List<int[]> mutableStatsList = new ArrayList<>(statsList);

        // Initialize with the first stats in the list
        if (!mutableStatsList.isEmpty()) {
            updateStats(mutableStatsList.get(0));
        }

        // Update stats every 1 second
        Timer timer = new Timer(8000, e -> {
            if (!mutableStatsList.isEmpty()) {
                int[] stats = mutableStatsList.remove(0); // Remove and update the first stats
                updateStats(stats);
            } else {
                ((Timer) e.getSource()).stop(); // Stop the timer if the list is empty
            }
        });
        timer.setInitialDelay(0); // Start immediately
        timer.start();
    }

    private void updateStats(int[] stats) {
        if (stats.length == 4) {
            playerDM = stats[0];
            playerEN = stats[1];
            opponentDM = stats[2];
            opponentEN = stats[3];
            repaint(); // Redraw panel with updated stats
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw character images
        drawCharacterImage(g, playerImage, 10, 30);
        drawCharacterImage(g, opponentImage, getWidth() - 90, 30);

        // Draw health and energy bars
        drawBar(g, "DM: " + playerDM + "/" + maxDM, playerDM, maxDM, 100, 30, Color.RED);
        drawBar(g, "EN: " + playerEN + "/" + maxEN, playerEN, maxEN, 100, 60, Color.YELLOW);
        drawBar(g, "DM: " + opponentDM + "/" + maxDM, opponentDM, maxDM, getWidth() - 400, 30, Color.RED);
        drawBar(g, "EN: " + opponentEN + "/" + maxEN, opponentEN, maxEN, getWidth() - 400, 60, Color.YELLOW);
    }

    private void drawCharacterImage(Graphics g, Image img, int x, int y) {
        if (img != null) {
            g.drawImage(img, x, y, 80, 80, this);
        }
    }

    private void drawBar(Graphics g, String label, int value, int max, int x, int y, Color color) {
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(x, y, 300, 20);
        g.setColor(color);
        int barWidth = (int) (300 * (value / (double) max));
        g.fillRect(x, y, barWidth, 20);
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

        // Example stats list
        List<int[]> statsList = List.of(
                new int[]{90, 100, 80, 100}, // First state
                new int[]{80, 90, 70, 90},  // Second state
                new int[]{70, 80, 60, 80},  // Third state
                new int[]{60, 70, 50, 70}   // Fourth state
        );

        frame.add(new HealthEnergyBarPanel(statsList));
        frame.setVisible(true);
    }
}
