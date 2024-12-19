//package org.example.ui;
//
//import java.awt.*;
//import java.util.List;
//import javax.swing.*;
//import org.example.card.AttackShape;
//import org.example.card.Card;
//import org.example.character.Character;
//import org.example.field.PlayField;
//import org.example.field.SceneCardPanel;
//
//public class Field {
//    public static void main(String[] args) {
//        // 메인 프레임 생성
//        JFrame frame = new JFrame("Grid Movement with GIF Swap and SceneCard Animation Test");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(1600, 800); // 화면 크기 설정 (너비 100%, 높이 80%)
//
//        // 프레임의 레이아웃을 BoxLayout으로 설정 (세로 방향)
//        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
//
//        // 캐릭터 설정
//        Character inuyasha = new Character(
//                "Inuyasha",
//                "src/main/resources/animations/cards/이누야샤기본모션200x160.gif",
//                "src/main/resources/animations/cards/이누야샤 점프200x160.gif",
//                "src/main/resources/animations/characters/이누야샤힐.gif",
//                "src/main/resources/animations/characters/이누야샤방어.gif",
//                "src/main/resources/animations/cards/이누야샤초가공격200x160.gif",
//                "src/main/resources/animations/cards/이누야샤기본공격200x160.gif",
//                "src/main/resources/animations/characters/이누야샤방어.gif",
//                new Point(0, 1)
//        );
//
//        Character kagome = new Character(
//                "Kagome",
//                "src/main/resources/animations/cards/이누야샤기본모션200x160.gif",
//                "src/main/resources/animations/cards/이누야샤 점프200x160.gif",
//                "src/main/resources/animations/characters/이누야샤힐.gif",
//                "src/main/resources/animations/characters/이누야샤방어.gif",
//                "src/main/resources/animations/cards/이누야샤초가공격200x160.gif",
//                "src/main/resources/animations/cards/이누야샤기본공격200x160.gif",
//                "src/main/resources/animations/characters/이누야샤방어.gif",
//                new Point(3, 1)
//        );
//
//        // 좌측 및 우측 플레이어 카드 생성
//        List<Card> leftCards = createLeftCards();
//        List<Card> rightCards = createRightCards();
//
//        // PlayField 설정 (A 부분) - 화면의 80% 차지
//        PlayField gamePanel = new PlayField(inuyasha, kagome, leftCards, rightCards);
//
//        // SceneCardPanel 설정 (B 부분) - 화면의 20% 차지
//        SceneCardPanel sceneCardPanel = new SceneCardPanel();
//        sceneCardPanel.initializeSceneCards(leftCards, rightCards);
//
//        // 각 패널 크기 설정
//        gamePanel.setMaximumSize(new Dimension(frame.getWidth(), (int) (frame.getHeight() * 0.7)));
//        sceneCardPanel.setMaximumSize(new Dimension(frame.getWidth(), (int) (frame.getHeight() * 0.3)));
//
//        // 패널에 여백 제거
//        gamePanel.setBorder(BorderFactory.createEmptyBorder());
//        sceneCardPanel.setBorder(BorderFactory.createEmptyBorder());
//
//        // PlayField는 위쪽에, SceneCardPanel은 아래쪽에 배치
//        frame.add(gamePanel);
//        frame.add(sceneCardPanel);
//
//        // 프레임 표시
//        frame.setVisible(true);
//        gamePanel.requestFocusInWindow(); // PlayField에 포커스를 맞춥니다.
//    }
//
//    // 좌측 플레이어 카드 생성
//    private static List<Card> createLeftCards() {
//        return List.of(
//                new Card(new org.example.card.CardData(
//                        org.example.card.CardType.ATTACK,
//                        10,
//                        5,
//                        "src/main/resources/animations/cards/test1.png",
//                        "src/main/resources/animations/cards/카드뒷면.png",
//                        false,
//                        null,
//                        org.example.card.AttackShape.H
//                )),
//                new Card(new org.example.card.CardData(
//                        org.example.card.CardType.ATTACK,
//                        15,
//                        7,
//                        "src/main/resources/animations/cards/test2.png",
//                        "src/main/resources/animations/cards/카드뒷면.png",
//                        false,
//                        null,
//                        org.example.card.AttackShape.H
//                )),
//                new Card(new org.example.card.CardData(
//                        org.example.card.CardType.DEFENSE,
//                        8,
//                        6,
//                        "src/main/resources/animations/cards/test3.png",
//                        "src/main/resources/animations/cards/카드뒷면.png",
//                        false,
//                        null,
//                        org.example.card.AttackShape.H
//                ))
//        );
//    }
//
//    // 우측 플레이어 카드 생성
//    private static List<Card> createRightCards() {
//        return List.of(
//                new Card(new org.example.card.CardData(
//                        org.example.card.CardType.DEFENSE,
//                        12,
//                        7,
//                        "src/main/resources/animations/cards/test4.png",
//                        "src/main/resources/animations/cards/카드뒷면.png",
//                        false,
//                        null,
//                        AttackShape.CROSS
//                )),
//                new Card(new org.example.card.CardData(
//                        org.example.card.CardType.ATTACK,
//                        18,
//                        9,
//                        "src/main/resources/animations/cards/test5.png",
//                        "src/main/resources/animations/cards/카드뒷면.png",
//                        false,
//                        null,
//                        AttackShape.X
//                )),
//                new Card(new org.example.card.CardData(
//                        org.example.card.CardType.DEFENSE,
//                        14,
//                        8,
//                        "src/main/resources/animations/cards/test6.png",
//                        "src/main/resources/animations/cards/카드뒷면.png",
//                        false,
//                        null,
//                        org.example.card.AttackShape.H
//                ))
//        );
//    }
//}
