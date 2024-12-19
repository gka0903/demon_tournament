package org.example.ui;

import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import org.example.card.Card;
import org.example.card.CardData;
import org.example.card.CardType;
import org.example.field.SceneCard;

public class SceneCardTurnTest {

    private JFrame frame;
    private JPanel rootPanel;
    private JPanel player1Panel;
    private JPanel player2Panel;
    private JLabel turnLabel;

    private List<SceneCard> player1Cards = new ArrayList<>(); // 플레이어 1의 SceneCard 리스트
    private List<SceneCard> player2Cards = new ArrayList<>(); // 플레이어 2의 SceneCard 리스트

    private boolean isPlayer1Turn = true; // 현재 턴 (true: 플레이어 1, false: 플레이어 2)
    private int player1Index = 0; // 플레이어 1의 현재 카드 인덱스
    private int player2Index = 0; // 플레이어 2의 현재 카드 인덱스

    public SceneCardTurnTest() {
        // 프레임 초기화
        frame = new JFrame("SceneCard Turn Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // 루트 패널 초기화
        rootPanel = new JPanel(null); // 절대 위치 지정 레이아웃
        frame.setContentPane(rootPanel);

        // UI 초기화
        initPlayerPanels();
        initTurnIndicator();

        // 카드 초기화
        initCards();

        // 프레임 표시
        frame.setVisible(true);
    }

    private void initPlayerPanels() {
        // 플레이어 1 패널
        player1Panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        player1Panel.setBounds(50, 400, 300, 150);
        player1Panel.setOpaque(false);
        rootPanel.add(player1Panel);

        // 플레이어 2 패널
        player2Panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        player2Panel.setBounds(450, 400, 300, 150);
        player2Panel.setOpaque(false);
        rootPanel.add(player2Panel);
    }

    private void initTurnIndicator() {
        // 턴 표시 레이블
        turnLabel = new JLabel("Player 1's Turn", SwingConstants.CENTER);
        turnLabel.setFont(new Font("Arial", Font.BOLD, 20));
        turnLabel.setBounds(300, 20, 200, 30);
        rootPanel.add(turnLabel);
    }

    private void initCards() {
        // 플레이어 1의 카드 생성 및 추가
        List<Card> player1MockCards = createMockCards("Player1_Card");
        for (Card card : player1MockCards) {
            JLabel cardLabel = new JLabel();
            SceneCard sceneCard = new SceneCard(cardLabel);
            sceneCard.setCardSprite(card);
            player1Cards.add(sceneCard);
            player1Panel.add(cardLabel);
        }

        // 플레이어 2의 카드 생성 및 추가
        List<Card> player2MockCards = createMockCards("Player2_Card");
        for (Card card : player2MockCards) {
            JLabel cardLabel = new JLabel();
            SceneCard sceneCard = new SceneCard(cardLabel);
            sceneCard.setCardSprite(card);
            player2Cards.add(sceneCard);
            player2Panel.add(cardLabel);
        }

        // 카드 클릭 이벤트 추가
        addCardClickListeners();
    }

    private void addCardClickListeners() {
        // 플레이어 1 카드 클릭
        for (int i = 0; i < player1Cards.size(); i++) {
            int index = i; // final 변수로 사용
            player1Cards.get(i).getCardImageView().addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (isPlayer1Turn && player1Index == index) {
                        player1Cards.get(index).useCard();
                        nextTurn();
                    }
                }
            });
        }

        // 플레이어 2 카드 클릭
        for (int i = 0; i < player2Cards.size(); i++) {
            int index = i; // final 변수로 사용
            player2Cards.get(i).getCardImageView().addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (!isPlayer1Turn && player2Index == index) {
                        player2Cards.get(index).useCard();
                        nextTurn();
                    }
                }
            });
        }
    }

    private void nextTurn() {
        if (isPlayer1Turn) {
            player1Index++; // 플레이어 1 카드 진행
            isPlayer1Turn = false; // 턴 변경
            turnLabel.setText("Player 2's Turn");
        } else {
            player2Index++; // 플레이어 2 카드 진행
            isPlayer1Turn = true; // 턴 변경
            turnLabel.setText("Player 1's Turn");
        }

        // 모든 카드를 사용했다면 종료
        if (player1Index == player1Cards.size() && player2Index == player2Cards.size()) {
            turnLabel.setText("Game Over!");
        }
    }

    private List<Card> createMockCards(String prefix) {
        // Mock 카드 데이터 생성
        List<Card> cards = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            cards.add(new Card(new CardData(
                    CardType.ATTACK,
                    10 * i, 5 * i,
                    "path/to/" + prefix + "_Front" + i + ".png",
                    "path/to/Back.png",
                    false,
                    null,
                    null
            )));
        }
        return cards;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SceneCardTurnTest::new);
    }
}
