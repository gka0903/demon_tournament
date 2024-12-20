package org.example.ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SelectedCardPanel extends JPanel {
    private final int maxSelectNum; // 최대 선택 가능한 카드 수
    private int currentSelectNum;  // 현재 선택된 카드 수
    private final List<JLabel> selectedCardLabels; // 선택된 카드 슬롯

    public SelectedCardPanel(int maxSelectNum) {
        this.maxSelectNum = maxSelectNum;
        this.currentSelectNum = 0;
        this.selectedCardLabels = new ArrayList<>();

        setLayout(new GridLayout(1, maxSelectNum));
        initSelectedCardSlots();
    }

    private void initSelectedCardSlots() {
        for (int i = 0; i < maxSelectNum; i++) {
            JLabel slot = new JLabel("Slot " + (i + 1), SwingConstants.CENTER);
            slot.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            selectedCardLabels.add(slot);
            add(slot);
        }
    }

    // 선택된 카드 슬롯 업데이트
    public void updateSelectedCard(ImageIcon cardImage, String cardType) {
        if (currentSelectNum >= maxSelectNum) return;

        JLabel slot = selectedCardLabels.get(currentSelectNum);
        slot.setIcon(cardImage);
        slot.setText(cardType);
        currentSelectNum++;
    }

    // 선택 초기화
    public void clearSelectedCards() {
        currentSelectNum = 0;
        for (JLabel slot : selectedCardLabels) {
            slot.setIcon(null);
            slot.setText("Slot " + (selectedCardLabels.indexOf(slot) + 1));
        }
    }

    public int getMaxSelectNum() {
        return maxSelectNum;
    }

    public int getCurrentSelectNum() {
        return currentSelectNum;
    }

    public List<String> getSelectedCards() {
        List<String> selectedCards = new ArrayList<>();
        for (JLabel slot : selectedCardLabels) {
            selectedCards.add(slot.getText());
        }
        return selectedCards;
    }

    public static void main(String[] args) {
        // JFrame을 생성하여 SelectedCardPanel을 테스트
        JFrame frame = new JFrame("Selected Card Panel Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 200); // 프레임 크기 설정

        // 최대 선택 가능한 카드 수 설정 (3개)
        SelectedCardPanel selectedCardPanel = new SelectedCardPanel(3);

        // 패널을 프레임에 추가
        frame.add(selectedCardPanel, BorderLayout.CENTER);

        // 카드 이미지로 사용할 임시 ImageIcon 생성 (테스트용)
        ImageIcon cardImage1 = new ImageIcon("src/main/resources/images/card1.png");
        ImageIcon cardImage2 = new ImageIcon("src/main/resources/images/card2.png");
        ImageIcon cardImage3 = new ImageIcon("src/main/resources/images/card3.png");

        // 버튼을 만들어 카드 선택 테스트
        JButton selectCardButton1 = new JButton("Select Card 1");
        selectCardButton1.addActionListener(e -> {
            selectedCardPanel.updateSelectedCard(cardImage1, "Attack");
        });

        JButton selectCardButton2 = new JButton("Select Card 2");
        selectCardButton2.addActionListener(e -> {
            selectedCardPanel.updateSelectedCard(cardImage2, "Defense");
        });

        JButton selectCardButton3 = new JButton("Select Card 3");
        selectCardButton3.addActionListener(e -> {
            selectedCardPanel.updateSelectedCard(cardImage3, "Magic");
        });

        JButton clearButton = new JButton("Clear Selection");
        clearButton.addActionListener(e -> {
            selectedCardPanel.clearSelectedCards();
        });

        // 버튼을 아래에 배치
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(selectCardButton1);
        buttonPanel.add(selectCardButton2);
        buttonPanel.add(selectCardButton3);
        buttonPanel.add(clearButton);

        frame.add(buttonPanel, BorderLayout.SOUTH); // 버튼 패널을 하단에 추가

        // 프레임 표시
        frame.setVisible(true);
    }
}
