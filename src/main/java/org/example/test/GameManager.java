package org.example.test;

import org.example.card.*;
import org.example.character.Character;
import org.example.field.PlayField;
import org.example.select.HealthEnergyBarPanel;
import org.example.field.SceneCardPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameManager {
    private final Character player1;
    private final Character player2;
    private final List<int[]> statsList; // StateManager로부터 가져온 상태 리스트
    private final JFrame frame;
    private PlayField playField;

    public GameManager(Character player1, Character player2, List<int[]> statsList) {
        this.player1 = player1;
        this.player2 = player2;
        this.statsList = statsList;

        this.frame = new JFrame("Game Manager");
        setupGame();
    }

    private void setupGame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600, 800);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        playField = new PlayField(player1, player2);

        // Health and Energy Bar Panel 설정 (상단 20%)
        HealthEnergyBarPanel healthEnergyBarPanel = new HealthEnergyBarPanel(statsList);
        healthEnergyBarPanel.setMaximumSize(new Dimension(frame.getWidth(), (int) (frame.getHeight() * 0.2)));

        playField.setMaximumSize(new Dimension(frame.getWidth(), (int) (frame.getHeight() * 0.5)));

        // SceneCardPanel 설정 (하단 30%)
        SceneCardPanel sceneCardPanel = new SceneCardPanel();
        sceneCardPanel.initializeSceneCards(player1.getCardList(), player2.getCardList());
        sceneCardPanel.setMaximumSize(new Dimension(frame.getWidth(), (int) (frame.getHeight() * 0.3)));

        frame.add(healthEnergyBarPanel);
        frame.add(playField);
        frame.add(sceneCardPanel);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // 캐릭터 초기화
        Character inuyasha = new Character(
                "Inuyasha",
                "src/main/resources/animations/cards/이누야샤기본모션200x160.gif",
                "src/main/resources/animations/cards/이누야샤 점프200x160.gif",
                "src/main/resources/animations/characters/이누야샤힐.gif",
                "src/main/resources/animations/characters/이누야샤방어.gif",
                "src/main/resources/animations/cards/이누야샤초가공격200x160.gif",
                "src/main/resources/animations/cards/이누야샤기본공격200x160.gif",
                "src/main/resources/animations/cards/이누야샤 점프200x160.gif",
                new Point(1, 1)
        );

        Character kagome = new Character(
                "Kagome",
                "src/main/resources/animations/cards/이누야샤기본모션200x160.gif",
                "src/main/resources/animations/cards/이누야샤 점프200x160.gif",
                "src/main/resources/animations/characters/이누야샤힐.gif",
                "src/main/resources/animations/characters/이누야샤방어.gif",
                "src/main/resources/animations/cards/이누야샤초가공격200x160.gif",
                "src/main/resources/animations/cards/이누야샤기본공격200x160.gif",
                "src/main/resources/animations/cards/이누야샤 점프200x160.gif",
                new Point(3, 1)
        );

        // 카드 추가
        inuyasha.addCard(new AttackCard(new CardData(CardType.ATTACK1, 10, 5, "", "", false, null, AttackShape.HORIZONTAL)));
        inuyasha.addCard(new Card(new CardData(CardType.MOVE, 0, 0, "", "", false, MoveDirection.RIGHT, null)));
        inuyasha.addCard(new AttackCard(new CardData(CardType.ATTACK2, 15, 7, "", "", false, null, AttackShape.HORIZONTAL)));

        kagome.addCard(new Card(new CardData(CardType.HEAL, 0, 0, "", "", false, null, null)));
        kagome.addCard(new Card(new CardData(CardType.MOVE, 0, 0, "", "", false, MoveDirection.LEFT, null)));
        kagome.addCard(new Card(new CardData(CardType.DEFENSE, 0, 0, "", "", false, null, null)));

        // StateManager 생성 및 상태 리스트 전달
        StateManagerTest stateManagerTest = new StateManagerTest(inuyasha, kagome);
        List<int[]> statsList = stateManagerTest.getStatsList();

        // GameManager 실행
        new GameManager(inuyasha, kagome, statsList);
    }
}
