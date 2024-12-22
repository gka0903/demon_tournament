package org.example.ui;

import java.util.List;
import org.example.card.*;
import org.example.character.Character;
import org.example.field.PlayField;
import org.example.field.SceneCardPanel;
import org.example.select.HealthEnergyBarPanel;

import javax.swing.*;
import java.awt.*;
import org.example.test.StateManagerTest;

public class Field {
    public static void main(String[] args) {
        // 메인 프레임 생성
        JFrame frame = new JFrame("Grid Movement with GIF Swap and SceneCard Animation Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600, 800); // 화면 크기 설정 (너비 100%, 높이 80%)

        // 프레임의 레이아웃을 BoxLayout으로 설정 (세로 방향)
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

//        java.util.List<int[]> statsList = List.of(
//                new int[]{100, 100, 100, 100}, // 첫 번째 상태
//                new int[]{90, 100, 80, 100}, // 첫 번째 상태
//                new int[]{80, 90, 70, 90},  // 두 번째 상태
//                new int[]{70, 80, 60, 80},  // 세 번째 상태
//                new int[]{60, 70, 50, 70}   // 네 번째 상태
//        );

        // 캐릭터 설정
        Character inuyasha = new Character(
                "Inuyasha",
                "src/main/resources/animations/cards/이누야샤/이누야샤기본모션200x160.gif",
                "src/main/resources/animations/cards/이누야샤/이누야샤 점프200x160.gif",
                "src/main/resources/animations/cards/이누야샤/이누야샤힐200x160.gif",
                "src/main/resources/animations/cards/이누야샤/이누야샤방어200x160.gif",
                "src/main/resources/animations/cards/이누야샤/이누야샤기본공격200x160.gif",
                "src/main/resources/animations/cards/이누야샤/이누야샤추가공격200x160.gif",
                "src/main/resources/animations/cards/이누야샤/이누야샤맞는모션200x160.gif",
                new Point(1, 1)
        );

        Character kagome = new Character(
                "Kagome",
                "src/main/resources/animations/cards/이누야샤/이누야샤기본모션200x160.gif",
                "src/main/resources/animations/cards/이누야샤/이누야샤 점프200x160.gif",
                "src/main/resources/animations/cards/이누야샤/이누야샤힐200x160.gif",
                "src/main/resources/animations/cards/이누야샤/이누야샤방어200x160.gif",
                "src/main/resources/animations/cards/이누야샤/이누야샤기본공격200x160.gif",
                "src/main/resources/animations/cards/이누야샤/이누야샤추가공격200x160.gif",
                "src/main/resources/animations/cards/이누야샤/이누야샤맞는모션200x160.gif",
                new Point(3, 1)
        );

        inuyasha.addCard(new Card(new CardData(CardType.MOVE, 0, 0, "", "", false, MoveDirection.RIGHT, null)));
        inuyasha.addCard(new AttackCard(new CardData(CardType.ATTACK1, 10, 5, "", "", false, null, AttackShape.HORIZONTAL)));
        inuyasha.addCard(new AttackCard(new CardData(CardType.ATTACK2, 15, 7, "", "", false, null, AttackShape.HORIZONTAL)));

        kagome.addCard(new Card(new CardData(CardType.HEAL, 0, 0, "", "", false, null, null)));
        kagome.addCard(new Card(new CardData(CardType.MOVE, 0, 0, "", "", false, MoveDirection.LEFT, null)));
        kagome.addCard(new Card(new CardData(CardType.DEFENSE, 0, 0, "", "", false, null, null)));

        // PlayField 설정 (중간 50%)
        PlayField gamePanel = new PlayField(inuyasha, kagome);

        StateManagerTest stateManagerTest = new StateManagerTest(inuyasha, kagome);
        List<int[]> statsList = stateManagerTest.getStatsList();

        for (int i = 0; i < statsList.size(); i++) {
            int[] stats = statsList.get(i);
            System.out.printf("Turn %d: P1[HP=%d, EN=%d], P2[HP=%d, EN=%d]%n",
                    i, stats[0], stats[1], stats[2], stats[3]);
        }

        // Health and Energy Bar Panel 설정 (상단 20%)
        HealthEnergyBarPanel healthEnergyBarPanel = new HealthEnergyBarPanel(statsList);
        healthEnergyBarPanel.setMaximumSize(new Dimension(frame.getWidth(), (int) (frame.getHeight() * 0.2)));

        gamePanel.setMaximumSize(new Dimension(frame.getWidth(), (int) (frame.getHeight() * 0.5)));

        // SceneCardPanel 설정 (하단 30%)
        SceneCardPanel sceneCardPanel = new SceneCardPanel();
        sceneCardPanel.initializeSceneCards(inuyasha.getCardList(), kagome.getCardList());
        sceneCardPanel.setMaximumSize(new Dimension(frame.getWidth(), (int) (frame.getHeight() * 0.3)));

        // 패널에 여백 제거
        gamePanel.setBorder(BorderFactory.createEmptyBorder());
        sceneCardPanel.setBorder(BorderFactory.createEmptyBorder());

        // 패널을 프레임에 추가
        frame.add(healthEnergyBarPanel); // 상단 패널
        frame.add(gamePanel); // 중간 패널
        frame.add(sceneCardPanel); // 하단 패널

        // 프레임 표시
        frame.setVisible(true);
        gamePanel.requestFocusInWindow(); // PlayField에 포커스를 맞춤
    }
}
