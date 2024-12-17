package org.example.test;

import org.example.card.AttackShape;
import org.example.card.Card;
import org.example.card.CardData;
import org.example.card.CardType;
import org.example.ui.SceneCard;

import javax.swing.*;
import java.awt.*;

public class SceneCardTest {
    public static void main(String[] args) {
        // 메인 프레임 생성
        JFrame frame = new JFrame("SceneCard Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new BorderLayout());

        // JLabel 생성 및 SceneCard 초기화
        JLabel cardLabel = new JLabel();
        cardLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cardLabel.setVerticalAlignment(SwingConstants.CENTER);

        SceneCard sceneCard = new SceneCard(cardLabel);

        // 테스트용 CardData 객체 생성
        CardData cardData = new CardData(
                CardType.ATTACK,       // 카드 타입
                10,                   // 데미지
                5,                    // 스태미나
                "src/main/resources/animations/cards/test8.png", // 카드 앞면 이미지 경로
                "src/main/resources/animations/cards/카드뒷면.png",  // 카드 뒷면 이미지 경로
                false,                // 카드 사용 여부
                null,                 // 이동 방향 (테스트용 null)
                AttackShape.H    // 공격 형태
        );

        // 테스트용 Card 객체 생성
        Card testCard = new Card(cardData);
        sceneCard.setCardSprite(testCard);

        // 버튼 생성
        JButton flipButton = new JButton("Flip Card");
        flipButton.addActionListener(e -> sceneCard.useCard());

        // UI 구성
        frame.add(cardLabel, BorderLayout.CENTER);
        frame.add(flipButton, BorderLayout.SOUTH);

        // 프레임 표시
        frame.setVisible(true);
    }
}
