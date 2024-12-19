package org.example.ui;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

import org.example.card.Card;

public class SceneCard {
    private JLabel cardImageView; // ImageView 대체
    private String front;
    private String back;

    public JLabel getCardImageView() {
        return cardImageView;
    }

    public SceneCard(JLabel cardImageView) {
        this.cardImageView = cardImageView;
    }

    // 카드 이미지 세팅
    public void setCardSprite(Card card) {
        this.front = card.getCardData().getCardSpritePath();
        this.back = card.getCardData().getCardBackSpritePath();

        cardImageView.setIcon(new ImageIcon(back)); // 초기에는 카드 뒷면을 표시
    }

    // 카드 사용할 때 호출
    public void useCard() {
        rotateCard();
    }

    // 카드 뒤집히는 애니메이션
    private void rotateCard() {
        final int[] step = {0}; // 현재 애니메이션 단계
        final int totalSteps = 20; // 애니메이션 단계 수
        final int delay = 25; // 각 단계당 지연 시간 (ms)

        Timer timer = new Timer();

        // TimerTask로 애니메이션 처리
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                step[0]++;

                // 회전 각도 계산
                double progress = (double) step[0] / totalSteps;
                double angle = progress * 180; // 180도 회전
                double scale = 1.0 - Math.abs(progress - 0.5) * 1.5; // 중간에 축소 효과

                // 애니메이션 적용
                if (step[0] <= totalSteps / 2) {
                    // 뒷면에서 앞면으로 변경
                    cardImageView.setIcon(new ImageIcon(back));
                } else {
                    cardImageView.setIcon(new ImageIcon(front));
                }

                // 애니메이션 완료 시 종료
                if (step[0] >= totalSteps) {
                    timer.cancel();
                }

                // UI 업데이트
                cardImageView.repaint();
            }
        };

        timer.scheduleAtFixedRate(task, 0, delay);
    }
}
