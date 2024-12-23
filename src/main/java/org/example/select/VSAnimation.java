package org.example.select;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VSAnimation {
    private JLabel playerImage, opponentImage, fightImage, backgroundLabel;
    private Timer playerTimer, opponentTimer;
    private int playerX = -200; // 내 캐릭터 시작 위치
    private int opponentX = 800; // 상대 캐릭터 시작 위치
    // 안 보이는 곳에서 시작하게 했습니다.

    public VSAnimation(Container c, String playerImagePath, String opponentImagePath) {
        // JLayeredPane 사용
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 800, 600);
        c.add(layeredPane);

        // 배경 이미지 추가
        ImageIcon backgroundIcon = new ImageIcon("src/main/resources/animations/select/배경화면.jpg");
        Image backgroundImg = backgroundIcon.getImage().getScaledInstance(800, 600, Image.SCALE_SMOOTH);
        backgroundLabel = new JLabel(new ImageIcon(backgroundImg));
        backgroundLabel.setBounds(0, 0, 800, 600);
        layeredPane.add(backgroundLabel, Integer.valueOf(0)); // 배경을 가장 뒤로 설정

        // 내 캐릭터 이미지
        ImageIcon playerIcon = new ImageIcon(playerImagePath);
        playerImage = new JLabel(new ImageIcon(
                playerIcon.getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH)
        ));
        playerImage.setBounds(playerX, 200, 300, 300); // 초기 위치
        layeredPane.add(playerImage, Integer.valueOf(2)); // 캐릭터는 배경 위로 설정

        // 상대 캐릭터 이미지
        ImageIcon opponentIcon = new ImageIcon(opponentImagePath);
        opponentImage = new JLabel(new ImageIcon(
                opponentIcon.getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH)
        ));
        opponentImage.setBounds(opponentX, 200, 300, 300); // 초기 위치
        layeredPane.add(opponentImage, Integer.valueOf(2)); // 캐릭터는 배경 위로 설정

        // 싸움 이미지 추가
        ImageIcon fightIcon = new ImageIcon("src/main/resources/animations/select/fight.png");
        Image fightImg = fightIcon.getImage().getScaledInstance(600, 300, Image.SCALE_SMOOTH);
        fightImage = new JLabel(new ImageIcon(fightImg));
        fightImage.setBounds(100, 0, 600, 300);
        layeredPane.add(fightImage, Integer.valueOf(1)); // 싸움 이미지는 캐릭터 위로 설정

        // 타이머를 설정해서 화면 안쪽으로 캐릭터 이미지들이 들어오게 합니다.
        playerTimer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (playerX < 0) { // 중앙으로 이동 중..
                    playerX += 5;
                    playerImage.setBounds(playerX, 200, 400, 400);
                } else {
                    playerTimer.stop();
                }
            }
        });

        opponentTimer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (opponentX > 400) { // 중앙으로 이동 중
                    opponentX -= 10;
                    opponentImage.setBounds(opponentX, 200, 400, 400);
                } else {
                    opponentTimer.stop();
                }
            }
        });

        // 애니메이션 시작
        playerTimer.start();
        opponentTimer.start();

        c.revalidate();
        c.repaint();
    }
}
