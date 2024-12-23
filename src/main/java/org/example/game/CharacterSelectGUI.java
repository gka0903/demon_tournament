package org.example.game;

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
        connectToServer(serverAddress, port, characterName); // 서버에 연결 및 데이터 전송

        // 대기 화면 추가 (예: "Waiting for other player...")
        JLabel waitingLabel = new JLabel("Waiting for other player...", SwingConstants.CENTER);
        waitingLabel.setFont(new Font("Serif", Font.BOLD, 24));
        waitingLabel.setForeground(Color.WHITE);
        getContentPane().removeAll();
        add(waitingLabel);
        revalidate();
        repaint();

        // 서버 응답 대기
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String response1 = in.readLine();
        String response2 = in.readLine();

        if (response1 != null && response2 != null) {
            System.out.println("Both players ready: " + response1 + ", " + response2);
            SwingUtilities.invokeLater(() -> {
                // 다음 화면으로 전환
                List<Card> cardList = CharacterCardList.valueOf(characterName).getCards();
                new CardSelectionAndChatView(characterName, serverAddress, port, cardList);
                dispose();
            });
        }
    }

    private void connectToServer(String serverAddress, int port, String characterName) throws IOException {
        // 서버 연결
        socket = new Socket(serverAddress, port);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println(characterName);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CharacterSelectGUI("127.0.0.1", 30000));
    }
}




