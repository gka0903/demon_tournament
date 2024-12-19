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
}
