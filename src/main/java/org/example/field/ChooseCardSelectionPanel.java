package org.example.field;

import org.example.card.AttackShape;
import org.example.card.Card;
import org.example.card.CardData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import org.example.card.CardType;

public class ChooseCardSelectionPanel extends JPanel {
    private List<JButton> cardButtons;
    private List<Card> cardList;
    private List<Card> selectedCards;
    private JLabel[] cardSlots;
    private JButton clearButton;
    private JButton continueButton;

    public ChooseCardSelectionPanel(List<Card> cardList) {
        this.cardList = cardList;
        this.selectedCards = new ArrayList<>();
        setLayout(new BorderLayout());

        // Card grid panel
        JPanel cardsGrid = new JPanel(new GridLayout(2, 4));
        cardButtons = new ArrayList<>();

        // Initialize card buttons
        initCards(cardsGrid);

        // Panel for card slots and buttons
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // Card slot panel for selected cards
        JPanel cardSlotPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        cardSlots = new JLabel[3]; // 3 card slots
        for (int i = 0; i < 3; i++) {
            cardSlots[i] = new JLabel("Place Card Here", SwingConstants.CENTER);
            cardSlots[i].setBorder(BorderFactory.createLineBorder(Color.GRAY));
            cardSlots[i].setPreferredSize(new Dimension(150, 200));
            cardSlots[i].setHorizontalAlignment(SwingConstants.CENTER);
            cardSlots[i].setVerticalAlignment(SwingConstants.CENTER);
            cardSlotPanel.add(cardSlots[i]);
        }
        cardSlotPanel.setBorder(BorderFactory.createTitledBorder("Selected Cards"));

        // Panel for buttons (20% width)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setPreferredSize(new Dimension(200, 150)); // Approximately 20% of 800px width

        // Clear button
        clearButton = new JButton("Clear");
        clearButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        clearButton.addActionListener(e -> clearSelection());

        // Continue button
        continueButton = new JButton("Continue");
        continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        continueButton.addActionListener(e -> displaySelectedCards());

        // Add buttons to button panel
        buttonPanel.add(Box.createVerticalGlue());
        buttonPanel.add(continueButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Spacer
        buttonPanel.add(clearButton);
        buttonPanel.add(Box.createVerticalGlue());

        // Add card slots and button panel to bottom panel
        bottomPanel.add(cardSlotPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        // Add panels to main layout
        add(cardsGrid, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Initialize cards and add to grid
    private void initCards(JPanel cardsGrid) {
        for (int i = 0; i < cardList.size(); i++) {
            Card card = cardList.get(i);
            ImageIcon cardImage = card.getCardImage();

            // Create card button
            JButton cardButton = new JButton(cardImage);
            cardButton.setToolTipText("<html>Type: " + card.getCardType() +
                    "<br>DM: " + card.getCardData().getDamage() +
                    "<br>EN: " + card.getCardData().getStamina() + "</html>");
            cardButton.setPreferredSize(new Dimension(150, 150));

            // Scale image
            Image img = cardImage.getImage();
            Image scaledImage = img.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            cardButton.setIcon(new ImageIcon(scaledImage));

            cardButton.addActionListener(new CardSelectionListener(cardButton, i));
            cardButtons.add(cardButton);
            cardsGrid.add(cardButton);
        }
    }

    // Listener for card selection
    private class CardSelectionListener implements ActionListener {
        private final JButton cardButton;
        private final int cardIndex;

        public CardSelectionListener(JButton cardButton, int cardIndex) {
            this.cardButton = cardButton;
            this.cardIndex = cardIndex;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedCards.size() < 3) {
                cardButton.setEnabled(false);
                Card selectedCard = cardList.get(cardIndex);
                selectedCards.add(selectedCard);
                updateCardSlots();
            } else {
                JOptionPane.showMessageDialog(null, "All slots are full! Clear the slots to add more cards.", "Slots Full", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    // Update card slots with selected cards
    private void updateCardSlots() {
        for (int i = 0; i < 3; i++) {
            if (i < selectedCards.size()) {
                Card card = selectedCards.get(i);
                Image img = card.getCardImage().getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);
                cardSlots[i].setIcon(new ImageIcon(img));
                cardSlots[i].setText(""); // Remove placeholder text
            } else {
                cardSlots[i].setIcon(null);
                cardSlots[i].setText("Place Card Here"); // Restore placeholder text
            }
        }
    }

    // Clear button functionality
    private void clearSelection() {
        selectedCards.clear();

        // Re-enable all card buttons
        for (JButton button : cardButtons) {
            button.setEnabled(true);
        }

        // Reset card slots
        updateCardSlots();
    }

    // Display selected cards when "Continue" is clicked
    private void displaySelectedCards() {
        if (selectedCards.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No cards selected!", "Empty Selection", JOptionPane.WARNING_MESSAGE);
        } else {
            StringBuilder message = new StringBuilder("Selected Cards:\n");
            for (Card card : selectedCards) {
                message.append("Type: ").append(card.getCardType())
                        .append(", Damage: ").append(card.getCardData().getDamage())
                        .append(", Stamina: ").append(card.getCardData().getStamina())
                        .append("\n");
            }
            JOptionPane.showMessageDialog(null, message.toString(), "Selected Cards", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Create test card data
        CardData cardData1 = new CardData(CardType.ATTACK1, 10, 5, "src/main/resources/animations/cards/test1.png", "src/main/resources/animations/cards/back.png", false, null, AttackShape.CROSS);
        CardData cardData2 = new CardData(CardType.DEFENSE, 8, 6, "src/main/resources/animations/cards/test2.png", "src/main/resources/animations/cards/back.png", false, null, null);
        CardData cardData3 = new CardData(CardType.ATTACK2, 12, 7, "src/main/resources/animations/cards/test3.png", "src/main/resources/animations/cards/back.png", false, null, null);
        CardData cardData4 = new CardData(CardType.DEFENSE, 6, 4, "src/main/resources/animations/cards/test4.png", "src/main/resources/animations/cards/back.png", false, null, null);
        CardData cardData5 = new CardData(CardType.ATTACK1, 20, 10, "src/main/resources/animations/cards/test5.png", "src/main/resources/animations/cards/back.png", false, null, null);
        CardData cardData6 = new CardData(CardType.DEFENSE, 18, 9, "src/main/resources/animations/cards/test6.png", "src/main/resources/animations/cards/back.png", false, null, null);
        CardData cardData7 = new CardData(CardType.ATTACK2, 15, 7, "src/main/resources/animations/cards/test7.png", "src/main/resources/animations/cards/back.png", false, null, null);
        CardData cardData8 = new CardData(CardType.DEFENSE, 10, 5, "src/main/resources/animations/cards/test8.png", "src/main/resources/animations/cards/back.png", false, null, null);

        List<Card> cardList = List.of(new Card(cardData1), new Card(cardData2), new Card(cardData3), new Card(cardData4),
                new Card(cardData5), new Card(cardData6), new Card(cardData7), new Card(cardData8));

        JFrame frame = new JFrame("Card Selection Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.add(new ChooseCardSelectionPanel(cardList));
        frame.setVisible(true);
    }
}
