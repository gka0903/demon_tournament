package org.example.connect;

import org.example.card.Card;
import org.example.characterEnum.CharacterCardList;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class CharacterSelectGUI extends JFrame {
    private Socket socket;

    public CharacterSelectGUI(String serverAddress, int port) {
        setTitle("Character Selection");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel instructionLabel = new JLabel("Select a Character", SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Serif", Font.BOLD, 24));
        instructionLabel.setBounds(250, 20, 300, 40);
        add(instructionLabel);

        String[] characters = {"INUYASHA", "SESSHOMARU"};
        String[] imagePaths = {
                "src/main/resources/animations/select/inuyasha.png",
                "src/main/resources/animations/select/maru.png"
        };

        JButton[] characterButtons = new JButton[characters.length];
        int buttonSize = 200;
        int startX = 200;
        int startY = 200;
        int gap = 300;

        for (int i = 0; i < characters.length; i++) {
            ImageIcon icon = new ImageIcon(imagePaths[i]);
            Image img = icon.getImage().getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH);
            icon = new ImageIcon(img);

            characterButtons[i] = new JButton(icon);
            characterButtons[i].setBounds(startX + (i * gap), startY, buttonSize, buttonSize);
            String characterName = characters[i];
            characterButtons[i].addActionListener(e -> {
                try {
                    connectToServer(serverAddress, port, characterName);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error connecting to server: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            add(characterButtons[i]);
        }

        setVisible(true);
    }

    private void connectToServer(String serverAddress, int port, String characterName) throws IOException {
        // 서버 연결
        socket = new Socket(serverAddress, port);
        System.out.println("Connected to server as: " + characterName);

        // 캐릭터에 맞는 카드 가져오기
        List<Card> cardList = CharacterCardList.valueOf(characterName).getCards();

        // 카드 선택 및 채팅 화면으로 이동
        SwingUtilities.invokeLater(() -> new CardSelectionAndChatView(characterName, serverAddress, port, cardList));
        dispose(); // 현재 창 닫기
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CharacterSelectGUI("127.0.0.1", 30000));
    }
}
