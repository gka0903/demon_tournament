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

        // 카드 UI 초기화
        initCardUI();

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

    private void initCardUI() {
        // 카드 이미지 로드
        loadCardImages();

        // 카드 패널 초기화
        cardPanel = new JPanel();
        cardPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        cardPanel.setOpaque(false); // 투명하게 설정하여 배경이 보이도록 함

        // 카드 이미지를 카드 패널에 추가
        for (ImageIcon cardImage : cardImages) {
            JLabel cardLabel = new JLabel(cardImage);
            cardPanel.add(cardLabel);
        }

        // 카드 패널의 위치와 크기 설정
        int panelWidth = frame.getWidth();
        int panelHeight = 200;
        int panelX = 0;
        int panelY = frame.getHeight() - panelHeight - 50; // 하단에 위치하도록 조정

        cardPanel.setBounds(panelX, panelY, panelWidth, panelHeight);

        // 배경 레이블에 카드 패널 추가
        backgroundLabel.add(cardPanel);
    }

    private void loadCardImages() {
        // 카드 이미지 로드 (실제 경로로 변경)
        cardImages.add(loadImage("src/main/resources/animations/cards/test1.png", 100, 150));
        cardImages.add(loadImage("src/main/resources/animations/cards/test2.png", 100, 150));
        cardImages.add(loadImage("src/main/resources/animations/cards/test3.png", 100, 150));
        // 필요한 만큼 카드 이미지를 추가
    }

    private ImageIcon loadImage(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CardUIBackgroundTest::new);
    }
}
