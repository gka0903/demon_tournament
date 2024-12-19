package org.example.field;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import org.example.card.Card;
import org.example.card.CardData;
import org.example.card.CardType;

public class SceneCardPanel extends JPanel {
    private JButton startButton;
    private List<SceneCard> leftCards;
    private List<SceneCard> rightCards;

    public SceneCardPanel() {
        setLayout(null); // Absolute layout 사용
        setBackground(Color.LIGHT_GRAY); // 배경색 설정
    }

    // 매개변수로 카드 데이터를 받아 SceneCard 초기화
    public void initializeSceneCards(List<Card> leftCardData, List<Card> rightCardData) {
        leftCards = new ArrayList<>();
        rightCards = new ArrayList<>();

        // 좌측 카드 생성
        for (int i = 0; i < leftCardData.size(); i++) {
            Card cardLeft = leftCardData.get(i);
            JLabel leftLabel = new JLabel();
            int leftX = 50 + i * 150; // 카드 위치 설정
            leftLabel.setBounds(leftX, 20, 100, 150);
            SceneCard leftSceneCard = new SceneCard(leftLabel);
            leftSceneCard.setCardSprite(cardLeft); // 카드 설정
            leftCards.add(leftSceneCard);
            add(leftLabel);
        }

        // 우측 카드 생성
        for (int i = 0; i < rightCardData.size(); i++) {
            Card cardRight = rightCardData.get(i);
            JLabel rightLabel = new JLabel();
            int rightX = 750 + i * 150; // 카드 위치 설정
            rightLabel.setBounds(rightX, 20, 100, 150);
            SceneCard rightSceneCard = new SceneCard(rightLabel);
            rightSceneCard.setCardSprite(cardRight); // 카드 설정
            rightCards.add(rightSceneCard);
            add(rightLabel);
        }

        // 카드 이동 타이머 설정
        Timer timer = new Timer(5000, null); // 5초 간격으로 카드 이동 시작
        timer.addActionListener(e -> {
            if (!leftCards.isEmpty() && !rightCards.isEmpty()) {
                SceneCard leftCard = leftCards.remove(leftCards.size() - 1); // 좌측 끝 카드
                SceneCard rightCard = rightCards.remove(0); // 우측 끝 카드
                moveCardsToCenter(leftCard, rightCard); // 카드 중앙으로 이동
            } else {
                startButton.setEnabled(true); // 모든 카드 이동 후 버튼 활성화
                ((Timer) e.getSource()).stop(); // 타이머 종료
            }
        });

        timer.start();
        timer.getActionListeners()[0].actionPerformed(null); // 첫 애니메이션 실행

        // 애니메이션 시작 버튼 추가
        startButton = new JButton("카드 고르기");
        startButton.setBounds(550, 170, 100, 50); // 버튼 위치 설정
        startButton.setEnabled(false); // 애니메이션이 끝날 때까지 비활성화
        startButton.addActionListener(e -> {
            System.out.println("화면 넘기기");
        });
        add(startButton);
    }

    // 카드 이동 로직
    private void moveCardsToCenter(SceneCard leftCard, SceneCard rightCard) {
        JLabel leftLabel = leftCard.getCardImageView();
        JLabel rightLabel = rightCard.getCardImageView();

        Point leftStart = leftLabel.getLocation();
        Point rightStart = rightLabel.getLocation();
        Point centerLeftTarget = new Point(500, 20); // 좌측 카드 중앙
        Point centerRightTarget = new Point(600, 20); // 우측 카드 중앙

        Timer animationTimer = new Timer(10, null); // 10ms마다 애니메이션 진행
        final int steps = 50; // 애니메이션 단계 수
        final int[] currentStep = {0}; // 현재 단계

        animationTimer.addActionListener(e -> {
            currentStep[0]++;

            // 좌표 보간 계산
            double progress = (double) currentStep[0] / steps;
            int leftX = (int) (leftStart.x + (centerLeftTarget.x - leftStart.x) * progress);
            int leftY = (int) (leftStart.y + (centerLeftTarget.y - leftStart.y) * progress);
            int rightX = (int) (rightStart.x + (centerRightTarget.x - rightStart.x) * progress);
            int rightY = (int) (rightStart.y + (centerRightTarget.y - rightStart.y) * progress);

            // 위치 업데이트
            leftLabel.setLocation(leftX, leftY);
            rightLabel.setLocation(rightX, rightY);

            if (currentStep[0] >= steps) {
                animationTimer.stop(); // 애니메이션 종료
                leftCard.useCard(); // 카드 사용
                rightCard.useCard(); // 카드 사용

                Timer removeTimer = new Timer(3500, evt -> {
                    remove(leftLabel); // 카드 제거
                    remove(rightLabel); // 카드 제거
                    repaint(); // 화면 갱신
                    ((Timer) evt.getSource()).stop();
                });
                removeTimer.start();
            }
        });

        animationTimer.start(); // 애니메이션 시작
    }

    // 이 패널을 화면 전체에 맞게 크기 설정
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1200, 300); // 적절한 크기 설정
    }

    public static void main(String[] args) {
        // 메인 프레임 생성
        JFrame frame = new JFrame("SceneCard Animation Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 300);
        frame.setLayout(new BorderLayout());

        // 카드 데이터 생성
        List<Card> leftCards = createTestCards(1, 3); // 좌측 카드 3장 생성
        List<Card> rightCards = createTestCards(4, 6); // 우측 카드 3장 생성

        // SceneCardPanel 초기화 및 카드 설정
        SceneCardPanel sceneCardPanel = new SceneCardPanel();
        sceneCardPanel.initializeSceneCards(leftCards, rightCards); // 카드 전달
        frame.add(sceneCardPanel, BorderLayout.CENTER);

        // 프레임 표시
        frame.setVisible(true);
    }

    // 테스트용 카드 생성 메서드
    private static List<Card> createTestCards(int startIndex, int endIndex) {
        List<Card> cards = new ArrayList<>();
        for (int i = startIndex; i <= endIndex; i++) {
            CardData cardData = new CardData(
                    CardType.ATTACK,
                    10 + i,
                    5 + i,
                    "src/main/resources/animations/cards/test" + i + ".png",
                    "src/main/resources/animations/cards/카드뒷면.png",
                    false,
                    null,
                    null
            );
            cards.add(new Card(cardData));
        }
        return cards;
    }
}
