package org.example.select;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CharacterSelectGUI extends JFrame {
    private JLabel selectedCharacterLabel;
    private JButton[] characterButtons;
    private String[] characters = {"Inuyasha", "Sesshomaru"};
    private String[] imagePaths = {
            "src/main/resources/animations/select/inuyasha.png",
            "src/main/resources/animations/select/maru.png"
    };

    public CharacterSelectGUI() {
        setTitle("Character Selection");
        setSize(800, 600); // 창 크기 동일하게 유지
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // 수동 레이아웃 설정

        Container c = getContentPane();

        selectedCharacterLabel = new JLabel("Select a Character", SwingConstants.CENTER);
        selectedCharacterLabel.setFont(new Font("Serif", Font.BOLD, 24));
        selectedCharacterLabel.setForeground(Color.BLACK);
        selectedCharacterLabel.setBounds(250, 20, 300, 40);
        c.add(selectedCharacterLabel);

        characterButtons = new JButton[characters.length];
        int buttonSize = 200; // 버튼 크기 조정
        int startX = 200; // 버튼 시작 위치 조정
        int startY = 200; // 버튼 Y 위치 조정
        int gap = 300; // 버튼 간 간격

        for (int i = 0; i < characters.length; i++) {
            ImageIcon icon = new ImageIcon(imagePaths[i]);
            Image img = icon.getImage().getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH);
            icon = new ImageIcon(img);

            characterButtons[i] = new JButton(icon);
            characterButtons[i].setBounds(startX + (i * gap), startY, buttonSize, buttonSize);
            characterButtons[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            characterButtons[i].addActionListener(new ButtonClickListener(characters[i]));
            c.add(characterButtons[i]);
        }

        setVisible(true);
    }

    private class ButtonClickListener implements ActionListener {
        private String characterName;

        public ButtonClickListener(String name) {
            this.characterName = name;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // 내가 선택한 캐릭터 이미지 경로
            String playerImagePath = "src/main/resources/animations/select/" + characterName.toLowerCase() + "1.jpeg";

            // 상대방 캐릭터 설정
            String opponentCharacter = "Sesshomaru".equals(characterName) ? "Inuyasha" : "Sesshomaru";
            String opponentImagePath = "src/main/resources/animations/select/" + opponentCharacter.toLowerCase() + "2.jpeg";

            getContentPane().removeAll();
            getContentPane().repaint();
            getContentPane().setLayout(null);
            new VSAnimation(getContentPane(), playerImagePath, opponentImagePath);
        }
    }

    public static void main(String[] args) {
        new CharacterSelectGUI();
    }
}
