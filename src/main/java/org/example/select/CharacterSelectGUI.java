package org.example.select;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class CharacterSelectGUI extends JFrame {
    private String selectedCharacter;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public CharacterSelectGUI(Socket socket, ObjectOutputStream out, ObjectInputStream in) {
        this.socket = socket;
        this.out = out;
        this.in = in;

        setTitle("Character Selection");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel instructionLabel = new JLabel("Select a Character", SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Serif", Font.BOLD, 24));
        instructionLabel.setBounds(250, 20, 300, 40);
        add(instructionLabel);

        String[] characters = {"Inuyasha", "Sesshomaru"};
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
                selectedCharacter = characterName;
                sendCharacterSelection(selectedCharacter);
            });
            add(characterButtons[i]);
        }

        setVisible(true);
    }

    private void sendCharacterSelection(String character) {
        try {
            // 서버로 선택한 캐릭터 전송
            out.writeObject(character);
            out.flush();

            // 서버로부터 상대방 캐릭터 정보 수신
            String opponentCharacter = (String) in.readObject();

            // 기존 컨텐츠 제거
            getContentPane().removeAll();
            getContentPane().repaint();
            getContentPane().setLayout(null);

            // VSAnimation 추가
            new VSAnimation(getContentPane(),
                    "src/main/resources/animations/select/" + character.toLowerCase() + "1.jpeg",
                    "src/main/resources/animations/select/" + opponentCharacter.toLowerCase() + "2.jpeg");

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

