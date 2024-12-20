package org.example.select;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CharacterSelectGUI extends JFrame {
    private JLabel selectedCharacterLabel;
    private JButton[] characterButtons;
    private String[] characters = {"Inuyasha", "Kagome", "Sango"};
    private String[] imagePaths = {
            "src/main/resources/animations/select/inuyasha.png",
            "src/main/resources/animations/select/kagome.png",
            "src/main/resources/animations/select/sango.png"
    };

    public CharacterSelectGUI() {
        setTitle("Character Selection");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); //버튼 테두리 크기가 자꾸 오류 나서 Layout을 Null로 받고 제가 수동으로 위치 받아왔습니다.

        Container c = getContentPane();

        selectedCharacterLabel = new JLabel("Select a Character", SwingConstants.CENTER);
        selectedCharacterLabel.setFont(new Font("Serif", Font.BOLD, 24));
        selectedCharacterLabel.setForeground(Color.BLACK);
        selectedCharacterLabel.setBounds(250, 20, 300, 40);
        c.add(selectedCharacterLabel);

        characterButtons = new JButton[characters.length];
        int buttonSize = 150;
        int startX = 100;
        int startY = 100;
        int gap = 200;
        //수동으로 위치 조정

        for (int i = 0; i < characters.length; i++) {
            ImageIcon icon = new ImageIcon(imagePaths[i]);
            Image img = icon.getImage().getScaledInstance(buttonSize, buttonSize, Image.SCALE_SMOOTH);
            icon = new ImageIcon(img);

            characterButtons[i] = new JButton(icon);
            characterButtons[i].setBounds(startX + (i * gap), startY, buttonSize, buttonSize);
            characterButtons[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            characterButtons[i].addActionListener(new ButtonClickListener(characters[i]));
            c.add(characterButtons[i]);
        } // 작업할 때 캐릭터 개수가 늘어날 수 있음을 고려하여 코드를 작성하였습니다.

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
            // 내가 선택한 캐릭터 이미지 경로
            String playerImagePath = "src/main/resources/animations/select/" + characterName.toLowerCase() + "1.jpeg";

            // 상대방이 선택한 캐릭터는 "Kagome"으로 고정 (연결하면 캐릭어 이름을 받아와야 합니다)
            String opponentCharacter = "Kagome";
            String opponentImagePath = "src/main/resources/animations/select/" + opponentCharacter.toLowerCase() + "2.jpeg";
            // 1과2로 나누어서 사진 대칭 조정했습니다.
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


