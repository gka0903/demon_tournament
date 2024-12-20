package org.example;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.example.card.CardData;
import org.example.ui.UICardView;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Card Selection View");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            // 카드 데이터 생성
            List<CardData> cards = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                cards.add(new CardData(
                        i % 2 == 0 ? org.example.card.CardType.ATTACK1 : org.example.card.CardType.DEFENSE,
                        10 + i, 5 + i,
                        "animations/cards/test" + (i + 1) + ".png",
                        "animations/cards/card_back.png",
                        false, null, null
                ));
            }

            // 카드 뷰 생성
            UICardView cardView = new UICardView(cards);
            frame.add(cardView);

            frame.setVisible(true);
        });
    }
}
