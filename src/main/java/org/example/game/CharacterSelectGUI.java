package org.example.game;

import org.example.card.Card;
import org.example.characterEnum.CharacterCardList;
import org.example.game.CardSelectionAndChatView;
import org.example.select.VSAnimation;

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

        // 배경 이미지 추가
        JLabel backgroundLabel = new JLabel(new ImageIcon(new ImageIcon("src/main/resources/animations/select/배경화면.jpg").getImage().getScaledInstance(800, 600, Image.SCALE_SMOOTH)));
        backgroundLabel.setBounds(0, 0, 800, 600);
        add(backgroundLabel);

        // 제목 라벨 꾸미기
        JLabel instructionLabel = new JLabel("Select a Character", SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Serif", Font.BOLD, 36));
        instructionLabel.setForeground(Color.WHITE);
        instructionLabel.setBounds(200, 20, 400, 50);
        backgroundLabel.add(instructionLabel); // 배경 위에 추가

        String[] characters = {"INUYASHA", "SESSHOMARU"};
        String[] imagePaths = {
                "src/main/resources/animations/select/inuyasha.png",
                "src/main/resources/animations/select/maru.png"
        };

        JButton[] characterButtons = new JButton[characters.length];
        int buttonSize = 200;
        int startX = 150;
        int startY = 150;
        int gap = 300;

        for (int i = 0; i < characters.length; i++) {
            ImageIcon icon = new ImageIcon(imagePaths[i]);
            Image img = icon.getImage().getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH);
            icon = new ImageIcon(img);

            characterButtons[i] = new JButton(icon);
            characterButtons[i].setBounds(startX + (i * gap), startY, buttonSize, buttonSize);
            characterButtons[i].setBackground(new Color(0, 0, 0, 0)); // 버튼 배경 투명
            characterButtons[i].setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            String characterName = characters[i];
            characterButtons[i].addActionListener(e -> {
                try {
                    playAnimation(characterName, serverAddress, port);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error connecting to server: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            backgroundLabel.add(characterButtons[i]); // 배경 위에 버튼 추가
        }

        // 하단 패널 추가
        JPanel footerPanel = new JPanel();
        footerPanel.setBounds(0, 550, 800, 50);
        footerPanel.setBackground(new Color(0, 0, 0, 150)); // 반투명 검정색

        JLabel footerLabel = new JLabel("© 2024 Inuyasha Demon tournament Game");
        footerLabel.setForeground(Color.WHITE);
        footerLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        footerPanel.add(footerLabel);

        backgroundLabel.add(footerPanel);

        setVisible(true);
    }

    private void playAnimation(String characterName, String serverAddress, int port) throws IOException {
        getContentPane().removeAll(); // 기존 선택 화면 제거
        repaint();

        JLayeredPane layeredPane = getLayeredPane();

        // 상대방 캐릭터 설정: 선택된 캐릭터와 다른 캐릭터로 설정
        String opponentCharacter = characterName.equals("INUYASHA") ? "SESSHOMARU" : "INUYASHA";

        // 애니메이션 추가
        Container animationContainer = new Container();
        animationContainer.setLayout(null);
        animationContainer.setBounds(0, 0, getWidth(), getHeight());
        new VSAnimation(animationContainer,
                "src/main/resources/animations/select/" + characterName.toLowerCase() + "1.jpeg",
                "src/main/resources/animations/select/" + opponentCharacter.toLowerCase() + "2.jpeg");
        layeredPane.add(animationContainer, JLayeredPane.POPUP_LAYER);

        Timer closeTimer = new Timer(3000, e -> {
            try {
                connectToServer(serverAddress, port, characterName);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error connecting to server: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        closeTimer.setRepeats(false);
        closeTimer.start();
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

