package org.example.test;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class DamageText extends JLabel {
    private final int moveSpeed = 2; // 텍스트가 이동하는 속도 (픽셀 단위)
    private final int destroyTime = 3000; // 텍스트가 사라지기까지의 시간 (밀리초)
    private Timer timer; // 애니메이션 및 삭제를 위한 타이머

    public DamageText(int damage) {
        super("-" + damage, SwingConstants.CENTER);
        setFont(new Font("Arial", Font.BOLD, 20));
        setForeground(Color.RED); // 텍스트 색상
        setOpaque(false); // 배경 투명

        startAnimation(); // 애니메이션 시작
    }

    private void startAnimation() {
        timer = new Timer();

        TimerTask moveTask = new TimerTask() {
            private int elapsed = 0;

            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    // 위로 이동
                    Point currentLocation = getLocation();
                    setLocation(currentLocation.x, currentLocation.y - moveSpeed);

                    elapsed += 50; // 50ms마다 호출
                    if (elapsed >= destroyTime) {
                        stopAnimation();
                    }
                });
            }
        };

        timer.scheduleAtFixedRate(moveTask, 0, 50); // 50ms 간격으로 실행
    }

    private void stopAnimation() {
        if (timer != null) {
            timer.cancel();
        }

        // 텍스트 제거
        SwingUtilities.invokeLater(() -> {
            Container parent = getParent();
            if (parent != null) {
                parent.remove(this);
                parent.repaint();
            }
        });
    }

    // 데미지 텍스트 생성 메서드
    public static DamageText createDamageText(int damage, Point startLocation, JComponent parent) {
        DamageText damageText = new DamageText(damage);
        damageText.setSize(100, 30); // 텍스트 크기 설정
        damageText.setLocation(startLocation);
        parent.add(damageText);
        parent.repaint();
        return damageText;
    }
}
