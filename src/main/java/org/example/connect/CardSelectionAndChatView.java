package org.example.connect;

import org.example.card.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import org.example.characterEnum.CharacterCardList;

public class CardSelectionAndChatView extends JFrame {
    private JTextArea chatArea;
    private JTextField chatInput;
    private List<JButton> cardButtons;
    private List<Card> cardList;
    private List<Card> selectedCards;
    private JLabel[] cardSlots;
    private JButton clearButton;
    private JButton sendButton;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String userName;

    public CardSelectionAndChatView(String userName, String serverAddress, int port, List<Card> cardList) {
        this.userName = userName;
        this.cardList = cardList;
        this.selectedCards = new ArrayList<>();

        setTitle("Card Selection and Chat");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Card Selection Panel
        ChooseCardSelectionPanel cardSelectionPanel = new ChooseCardSelectionPanel(cardList);
        add(cardSelectionPanel, BorderLayout.NORTH);

        // Chat Panel
        JPanel chatPanel = new JPanel(new BorderLayout());
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        chatPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel chatInputPanel = new JPanel(new BorderLayout());
        chatInput = new JTextField();
        sendButton = new JButton("Send Cards");
        chatInputPanel.add(chatInput, BorderLayout.CENTER);
        chatInputPanel.add(sendButton, BorderLayout.EAST);
        chatPanel.add(chatInputPanel, BorderLayout.SOUTH);

        add(chatPanel, BorderLayout.CENTER);

        // Server connection
        connectToServer(serverAddress, port);

        // Button actions
        clearButton = cardSelectionPanel.getClearButton();
        clearButton.addActionListener(e -> cardSelectionPanel.clearSelection());
        sendButton.addActionListener(e -> sendSelectedCards(cardSelectionPanel.getSelectedCardNames()));

        new Thread(new Listener()).start();

        setVisible(true);
    }

    private void connectToServer(String serverAddress, int port) {
        try {
            socket = new Socket(serverAddress, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println(userName + " has joined the chat.");
            chatArea.append("Connected to server.\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Unable to connect to server: " + e.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void sendSelectedCards(List<String> cardNames) {
        if (cardNames.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No cards selected to send!", "Empty Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String listString = String.join(",", cardNames);
        out.println("Card List: " + listString);
        chatArea.append("Sent Card List: " + listString + "\n");
    }

    private class Listener implements Runnable {
        @Override
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    chatArea.append(message + "\n");
                    chatArea.setCaretPosition(chatArea.getDocument().getLength());
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(CardSelectionAndChatView.this, "Disconnected from server.", "Connection Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        }
    }

    // Inner Class for Card Selection Panel
    private class ChooseCardSelectionPanel extends JPanel {
        private List<JButton> cardButtons;
        private List<Card> cardList;
        private List<Card> selectedCards;
        private JLabel[] cardSlots;
        private JButton clearButton;

        public ChooseCardSelectionPanel(List<Card> cardList) {
            this.cardList = cardList;
            this.selectedCards = new ArrayList<>();
            setLayout(new BorderLayout());

            // Card grid panel
            JPanel cardsGrid = new JPanel(new GridLayout(2, 4));
            cardButtons = new ArrayList<>();
            initCards(cardsGrid);

            // Panel for card slots and buttons
            JPanel bottomPanel = new JPanel(new BorderLayout());

            JPanel cardSlotPanel = new JPanel(new GridLayout(1, 3, 10, 0));
            cardSlots = new JLabel[3];
            for (int i = 0; i < 3; i++) {
                cardSlots[i] = new JLabel("Place Card Here", SwingConstants.CENTER);
                cardSlots[i].setBorder(BorderFactory.createLineBorder(Color.GRAY));
                cardSlots[i].setPreferredSize(new Dimension(150, 200));
                cardSlots[i].setHorizontalAlignment(SwingConstants.CENTER);
                cardSlots[i].setVerticalAlignment(SwingConstants.CENTER);
                cardSlotPanel.add(cardSlots[i]);
            }
            cardSlotPanel.setBorder(BorderFactory.createTitledBorder("Selected Cards"));

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
            buttonPanel.setPreferredSize(new Dimension(200, 150));

            clearButton = new JButton("Clear");
            clearButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            clearButton.addActionListener(e -> clearSelection());

            buttonPanel.add(Box.createVerticalGlue());
            buttonPanel.add(clearButton);
            buttonPanel.add(Box.createVerticalGlue());

            bottomPanel.add(cardSlotPanel, BorderLayout.CENTER);
            bottomPanel.add(buttonPanel, BorderLayout.EAST);

            add(cardsGrid, BorderLayout.CENTER);
            add(bottomPanel, BorderLayout.SOUTH);
        }

        private void initCards(JPanel cardsGrid) {
            for (int i = 0; i < cardList.size(); i++) {
                Card card = cardList.get(i);
                ImageIcon cardImage = card.getCardImage();

                JButton cardButton = new JButton(cardImage);
                cardButton.setToolTipText("<html>Type: " + card.getCardType() +
                        "<br>DM: " + card.getCardData().getDamage() +
                        "<br>EN: " + card.getCardData().getStamina() + "</html>");
                cardButton.setPreferredSize(new Dimension(150, 150));
                cardButton.addActionListener(new CardSelectionListener(cardButton, i));
                cardButtons.add(cardButton);
                cardsGrid.add(cardButton);
            }
        }

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
                    selectedCards.add(cardList.get(cardIndex));
                    updateCardSlots();
                } else {
                    JOptionPane.showMessageDialog(null, "All slots are full! Clear the slots to add more cards.", "Slots Full", JOptionPane.WARNING_MESSAGE);
                }
            }
        }

        private void updateCardSlots() {
            for (int i = 0; i < 3; i++) {
                if (i < selectedCards.size()) {
                    Card card = selectedCards.get(i);
                    Image img = card.getCardImage().getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);
                    cardSlots[i].setIcon(new ImageIcon(img));
                    cardSlots[i].setText("");
                } else {
                    cardSlots[i].setIcon(null);
                    cardSlots[i].setText("Place Card Here");
                }
            }
        }

        public void clearSelection() {
            selectedCards.clear();
            for (JButton button : cardButtons) {
                button.setEnabled(true);
            }
            updateCardSlots();
        }

        public List<String> getSelectedCardNames() {
            List<String> cardNames = new ArrayList<>();
            for (Card card : selectedCards) {
                cardNames.add(card.getCardType().toString());
            }
            return cardNames;
        }

        public JButton getClearButton() {
            return clearButton;
        }
    }

    public static void main(String[] args) {
        List<Card> cardList = CharacterCardList.SESSHOMARU.getCards();
        new CardSelectionAndChatView("Player1", "127.0.0.1", 30000, cardList);
    }
}
