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

    private String player1Character = null;  // 플레이어 1 캐릭터
    private String player2Character = null;  // 플레이어 2 캐릭터

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

    // 버튼 클릭 이벤트 처리 부분입니다.
    private class ButtonClickListener implements ActionListener {
        private String characterName;

        public ButtonClickListener(String name) {
            this.characterName = name;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // 플레이어 1의 캐릭터 선택
            if (player1Character == null) {
                player1Character = characterName;  // 플레이어 1 캐릭터 설정
                System.out.println("Player 1 selected: " + characterName);
                updateLabel("Player 1 selected " + characterName + ". Now, Player 2 selects a character.");
            }
            // 플레이어 2의 캐릭터 선택
            else if (player2Character == null) {
                if (characterName.equals(player1Character)) {
                    JOptionPane.showMessageDialog(null, "Player 2 cannot select the same character as Player 1.");
                } else {
                    player2Character = characterName;  // 플레이어 2 캐릭터 설정
                    System.out.println("Player 2 selected: " + characterName);
                    updateLabel("Player 2 selected " + characterName + ". Starting VS animation...");
                    // VS 애니메이션 화면으로 넘어가기
                    startVSAnimation(player1Character, player2Character);
                }
            }
        }
    }

    private void updateLabel(String text) {
        selectedCharacterLabel.setText(text);  // 선택된 캐릭터 표시
    }

    private void startVSAnimation(String player1Character, String player2Character) {
        // 캐릭터 선택 후 VS 애니메이션 화면으로 전환
        getContentPane().removeAll();
        getContentPane().repaint();
        getContentPane().setLayout(null);

        // 플레이어 1과 2의 캐릭터 이미지를 가져와서 VS 애니메이션 화면에 전달
        String player1ImagePath = "src/main/resources/animations/select/" + player1Character.toLowerCase() + "1.jpeg";
        String player2ImagePath = "src/main/resources/animations/select/" + player2Character.toLowerCase() + "2.jpeg";

        // VS 애니메이션 창으로 이동
        new VSAnimation(getContentPane(), player1ImagePath, player2ImagePath);
    }

    public static void main(String[] args) {
        new CharacterSelectGUI();
    }
}
