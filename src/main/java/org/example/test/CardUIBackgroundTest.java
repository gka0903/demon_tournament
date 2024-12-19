package org.example.test;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CardUIBackgroundTest {

    private JFrame frame;
    private JPanel rootPanel;
    private JLabel backgroundLabel;
    private JPanel cardPanel;

    private List<ImageIcon> cardImages = new ArrayList<>();

    public CardUIBackgroundTest() {
        // 프레임 초기화
        frame = new JFrame("카드 UI 및 배경 UI 테스트");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // 루트 패널 초기화
        rootPanel = new JPanel(null); // 절대 위치 지정 레이아웃
        frame.setContentPane(rootPanel);

        // 배경 초기화
        initBackground();

        // 프레임 표시
        frame.setVisible(true);
    }

    private void initBackground() {
        // 배경 이미지 로드
        ImageIcon backgroundImage = new ImageIcon("src/main/resources/animations/ui/ㅇ.png"); // 실제 경로로 변경
        backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, 0, frame.getWidth(), frame.getHeight());

        // 배경 레이블에 레이아웃 설정
        backgroundLabel.setLayout(null);

        // 루트 패널에 배경 레이블 추가
        rootPanel.add(backgroundLabel);
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(CardUIBackgroundTest::new);
    }
}
