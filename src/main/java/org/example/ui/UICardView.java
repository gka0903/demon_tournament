package org.example.ui;

import org.example.card.CardData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class UICardView extends JPanel {
    private JPanel cardPanel; // 카드 버튼 패널
    private JButton continueButton; // "Continue" 버튼
    private JButton clearButton; // "Clear" 버튼

    private List<JButton> cardButtons; // 카드 버튼 리스트
    private List<CardData> cardDataList; // 카드 데이터 리스트
    private SelectedCardPanel selectedCardPanel; // 선택된 카드 패널

    public UICardView(List<CardData> cardDataList) {
        this.cardDataList = cardDataList;
        setLayout(new BorderLayout());

        // 선택된 카드 패널 생성
        selectedCardPanel = new SelectedCardPanel(3); // 최대 3장 선택 가능

        // 카드 패널 초기화 (2행 4열)
        cardPanel = new JPanel(new GridLayout(2, 4));
        cardButtons = new ArrayList<>();

        // 버튼 초기화
        continueButton = new JButton("Continue");
        continueButton.setEnabled(false); // 초기 상태에서 비활성화
        continueButton.addActionListener(e -> onClickContinueButton());

        clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> onClickClearButton());

        // 버튼 패널 추가
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(continueButton);
        buttonPanel.add(clearButton);

        // 카드 및 선택된 카드 슬롯 생성
        initCards();

        // 전체 패널 구성
        add(selectedCardPanel, BorderLayout.NORTH); // 선택된 카드 패널 상단
        add(cardPanel, BorderLayout.CENTER); // 카드 버튼 패널 중앙
        add(buttonPanel, BorderLayout.SOUTH); // 버튼 패널 하단
    }

    // 카드 버튼 초기화
    private void initCards() {
        for (int i = 0; i < cardDataList.size(); i++) {
            CardData cardData = cardDataList.get(i);

            // 카드 이미지 로드
            ImageIcon cardImage = loadImage(cardData.getCardSpritePath());
            if (cardImage == null) {
                System.err.println("Card image not found for: " + cardData.getCardSpritePath());
            }

            // 카드 버튼 생성
            JButton cardButton = new JButton(cardImage); // 카드 이미지 설정
            cardButton.setToolTipText("<html>Type: " + cardData.getCardType() +
                    "<br>DM: " + cardData.getDamage() +
                    "<br>EN: " + cardData.getStamina() + "</html>"); // 카드 데이터 툴팁 표시
            cardButton.addActionListener(new CardSelectionListener(cardButton, i));
            cardButtons.add(cardButton);
            cardPanel.add(cardButton);
        }
    }

    // 이미지 로드
    private ImageIcon loadImage(String path) {
        try {
            java.net.URL imgURL = getClass().getClassLoader().getResource(path);
            if (imgURL != null) {
                return new ImageIcon(imgURL);
            } else {
                System.err.println("이미지 경로를 찾을 수 없습니다: " + path);
                return null;
            }
        } catch (Exception e) {
            System.err.println("이미지 로드 중 오류 발생: " + path);
            e.printStackTrace();
            return null;
        }
    }

    // "Continue" 버튼 동작
    private void onClickContinueButton() {
        System.out.println("Continue button clicked!");

        // 선택된 카드 데이터 출력
        System.out.println("Selected Cards:");
        selectedCardPanel.getSelectedCards().forEach(System.out::println);
    }

    // "Clear" 버튼 동작
    private void onClickClearButton() {
        System.out.println("Clear button clicked!");
        selectedCardPanel.clearSelectedCards(); // 선택 초기화
        for (JButton button : cardButtons) {
            button.setEnabled(true); // 카드 버튼 활성화
        }
        continueButton.setEnabled(false);
    }

    // 카드 선택 리스너
    private class CardSelectionListener implements ActionListener {
        private final JButton cardButton;
        private final int cardIndex;

        public CardSelectionListener(JButton cardButton, int cardIndex) {
            this.cardButton = cardButton;
            this.cardIndex = cardIndex;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedCardPanel.getCurrentSelectNum() >= selectedCardPanel.getMaxSelectNum()) {
                return; // 선택 제한 초과
            }

            CardData selectedCard = cardDataList.get(cardIndex);

            // 선택된 카드 패널 업데이트
            selectedCardPanel.updateSelectedCard(
                    loadImage(selectedCard.getCardSpritePath()),
                    selectedCard.getCardType().toString()
            );

            cardButton.setEnabled(false); // 선택된 카드 버튼 비활성화
            if (selectedCardPanel.getCurrentSelectNum() == selectedCardPanel.getMaxSelectNum()) {
                continueButton.setEnabled(true);
            }
        }
    }
}
