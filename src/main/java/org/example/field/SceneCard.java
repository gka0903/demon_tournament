package org.example.field;

import java.awt.Image;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

import org.example.card.Card;

public class SceneCard {
    private final JLabel cardImageView; // 카드 이미지를 표시할 JLabel
    private String frontImagePath; // 카드 앞면 이미지 경로
    private String backImagePath;  // 카드 뒷면 이미지 경로

    // 카드 이미지 뷰 반환
    public JLabel getCardImageView() {
        return cardImageView;
    }

    // SceneCard 생성자
    public SceneCard(JLabel cardImageView) {
        this.cardImageView = cardImageView;
    }

    // 카드 이미지 세팅
    public void setCardSprite(Card card) {
        this.frontImagePath = card.getCardData().getCardSpritePath();
        this.backImagePath = card.getCardData().getCardBackSpritePath();

        // 초기 이미지를 카드 뒷면으로 설정
        ImageIcon icon = new ImageIcon(backImagePath);
        Image img = icon.getImage().getScaledInstance(
                cardImageView.getWidth(),
                cardImageView.getHeight(),
                Image.SCALE_SMOOTH
        );
        cardImageView.setIcon(new ImageIcon(img)); // 크기 조정된 이미지를 설정
    }

    // 카드 사용 (뒤집기 애니메이션 호출)
    public void useCard() {
        rotateCard();
    }

    // 카드 뒤집히는 애니메이션 처리
    private void rotateCard() {
        final int totalSteps = 50; // 0.5초 동안 진행되는 애니메이션 단계 수
        final int delay = 10;       // 각 단계당 지연 시간(ms) (0.5초 동안 끝내기 위해)
        final int[] currentStep = {0}; // 현재 애니메이션 단계 추적

        Timer timer = new Timer();

        // 애니메이션 단계별 처리
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                currentStep[0]++;

                // 카드 이미지 변경 시점
                if (currentStep[0] == totalSteps / 2) {
                    // 중간 단계에서 앞면으로 변경
                    ImageIcon icon = new ImageIcon(frontImagePath);
                    Image img = icon.getImage().getScaledInstance(
                            cardImageView.getWidth(),
                            cardImageView.getHeight(),
                            Image.SCALE_SMOOTH
                    );
                    cardImageView.setIcon(new ImageIcon(img)); // 크기 조정된 이미지를 설정
                }

                // 애니메이션 종료 조건
                if (currentStep[0] >= totalSteps) {
                    timer.cancel(); // 타이머 종료
                    // 카드가 뒤집힌 후 대기 애니메이션 호출
                    waitForFurtherAction();
                }

                // UI 업데이트
                cardImageView.repaint();
            }
        };

        // 타이머로 애니메이션 실행 (0.5초 동안)
        timer.scheduleAtFixedRate(task, 0, delay);
    }

    // 카드가 뒤집힌 후 7.5초 대기
    private void waitForFurtherAction() {
        // 7.5초 대기
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                // 대기 시간이 끝난 후 추가적인 동작을 진행할 수 있음
                // 예시: 추가 작업이나 애니메이션 등
                // 대기 후 처리할 작업을 넣어주세요.
            }
        };

        // 7.5초 동안 대기 (7500ms)
        timer.schedule(task, 15000);
    }
}
