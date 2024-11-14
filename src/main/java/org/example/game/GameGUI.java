package org.example.game;

import javax.swing.*;
import java.awt.*;
import org.example.cards.*;
import org.example.players.Player;
import org.example.game.Game;

public class GameGUI extends JFrame {
    private Game game;
    private JTextArea gameLog;
    private JPanel fieldPanel;
    private JLabel[][] fieldCells;
    private JLabel[] selectedCardLabels;
    private int selectedCardIndex = 0;

    public GameGUI(Player player1, Player player2) {
        game = new Game(player1, player2);
        setTitle("Game");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 필드 생성 (4x3 격자)
        fieldPanel = new JPanel(new GridLayout(4, 3));
        fieldCells = new JLabel[4][3];
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 3; col++) {
                fieldCells[row][col] = new JLabel();
                fieldCells[row][col].setPreferredSize(new Dimension(60, 60));
                fieldCells[row][col].setBorder(BorderFactory.createLineBorder(Color.GRAY));
                fieldPanel.add(fieldCells[row][col]);
            }
        }
        add(fieldPanel, BorderLayout.CENTER);

        // 플레이어 위치 초기화
        updatePlayerPositions();

        // 카드 선택 패널 생성
        JPanel cardSelectionPanel = new JPanel();
        JButton attackButton = new JButton("Basic Attack");
        JButton guardButton = new JButton("Guard");
        JButton energyButton = new JButton("Energy");
        JButton longRangeAttackButton = new JButton("Long Range Attack");
        JButton moveUpButton = new JButton("Move Up");
        JButton moveDownButton = new JButton("Move Down");
        JButton moveLeftButton = new JButton("Move Left");
        JButton moveRightButton = new JButton("Move Right");

        attackButton.addActionListener(e -> selectCard(game.getPlayer1(), new BasicAttackCard()));
        guardButton.addActionListener(e -> selectCard(game.getPlayer1(), new GuardCard()));
        energyButton.addActionListener(e -> selectCard(game.getPlayer1(), new EnergyCard(15)));
        longRangeAttackButton.addActionListener(e -> selectCard(game.getPlayer1(), new LongRangeAttackCard()));
        moveUpButton.addActionListener(e -> selectCard(game.getPlayer1(), new MoveCards.MoveUpCard()));
        moveDownButton.addActionListener(e -> selectCard(game.getPlayer1(), new MoveCards.MoveDownCard()));
        moveLeftButton.addActionListener(e -> selectCard(game.getPlayer1(), new MoveCards.MoveLeftCard()));
        moveRightButton.addActionListener(e -> selectCard(game.getPlayer1(), new MoveCards.MoveRightCard()));

        cardSelectionPanel.add(attackButton);
        cardSelectionPanel.add(guardButton);
        cardSelectionPanel.add(energyButton);
        cardSelectionPanel.add(longRangeAttackButton);
        cardSelectionPanel.add(moveUpButton);
        cardSelectionPanel.add(moveDownButton);
        cardSelectionPanel.add(moveLeftButton);
        cardSelectionPanel.add(moveRightButton);

        add(cardSelectionPanel, BorderLayout.SOUTH);

        // 카드 선택 미리보기 영역
        selectedCardLabels = new JLabel[3];
        JPanel selectedCardsPanel = new JPanel();
        for (int i = 0; i < 3; i++) {
            selectedCardLabels[i] = new JLabel(new ImageIcon("card_back.png"));
            selectedCardLabels[i].setPreferredSize(new Dimension(80, 100));
            selectedCardsPanel.add(selectedCardLabels[i]);
        }
        add(selectedCardsPanel, BorderLayout.NORTH);

        JButton nextTurnButton = new JButton("Use Next Card");
        nextTurnButton.addActionListener(e -> useNextCard());
        add(nextTurnButton, BorderLayout.EAST);

        gameLog = new JTextArea();
        gameLog.setEditable(false);
        add(new JScrollPane(gameLog), BorderLayout.WEST);
    }

    private void selectCard(Player player, Card card) {
        boolean success = player.chooseCard(card);
        if (success) {
            updateGameLog(player.getName() + " selected " + card.getName());
            selectedCardLabels[player.getDeck().size() - 1].setIcon(new ImageIcon("card_" + card.getName() + ".png"));
        } else {
            updateGameLog("Could not select " + card.getName() + " (limit reached or insufficient energy).");
        }
    }

    private void updatePlayerPositions() {
        // 필드 초기화
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 3; col++) {
                fieldCells[row][col].setIcon(null);
            }
        }
        fieldCells[game.getPlayer1().getPositionX()][game.getPlayer1().getPositionY()]
                .setIcon(new ImageIcon("player1.png"));
        fieldCells[game.getPlayer2().getPositionX()][game.getPlayer2().getPositionY()]
                .setIcon(new ImageIcon("player2.png"));
    }

    private void useNextCard() {
        if (selectedCardIndex < 3) {
            Card card = game.getPlayer1().useNextCard(game.getPlayer2());
            if (card != null) {
                selectedCardLabels[selectedCardIndex].setIcon(new ImageIcon("card_" + card.getName() + ".png"));
                updateGameLog(game.getPlayer1().getName() + " uses " + card.getName());
                updatePlayerPositions();
                selectedCardIndex++;
            }

            card = game.getPlayer2().useNextCard(game.getPlayer1());
            if (card != null) {
                updateGameLog(game.getPlayer2().getName() + " uses " + card.getName());
                updatePlayerPositions();
            }

            if (selectedCardIndex >= 3) {
                resetForNewTurn();
            }
        }
    }

    private void resetForNewTurn() {
        selectedCardIndex = 0;
        for (JLabel label : selectedCardLabels) {
            label.setIcon(new ImageIcon("card_back.png"));
        }
        game.getPlayer1().resetTurn();
        game.getPlayer2().resetTurn();
        updateGameLog("New turn started. Select cards again.");
    }

    private void updateGameLog(String message) {
        gameLog.append(message + "\n");
    }

    public static void main(String[] args) {
        Player player1 = new Player("Player 1", 0, 1);
        Player player2 = new Player("Player 2", 3, 1);

        SwingUtilities.invokeLater(() -> {
            GameGUI gui = new GameGUI(player1, player2);
            gui.setVisible(true);
        });
    }
}
