package org.example.test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameStartUI extends JFrame {

    private JButton startButton;
    private JLabel statusLabel;
    private boolean player1Ready = false;
    private boolean player2Ready = false;

    public GameStartUI() {
        // 게임 시작 UI 구성
        setTitle("Game Start");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 상태 라벨 (플레이어 연결 및 상태 표시)
        statusLabel = new JLabel("Waiting for players to connect...", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Serif", Font.BOLD, 18));
        add(statusLabel, BorderLayout.CENTER);

        // 시작 버튼
        startButton = new JButton("Start Game");
        startButton.setFont(new Font("Serif", Font.BOLD, 20));
        startButton.setEnabled(false); // 게임 시작 버튼 비활성화 상태로 시작

        // 버튼 클릭 이벤트 처리
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 게임 시작 버튼 클릭 시 게임을 시작하는 로직
                startGame();
            }
        });
        add(startButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    // 서버에서 플레이어 1과 플레이어 2 연결 시 호출
    public void playerConnected(int playerNumber) {
        if (playerNumber == 1) {
            player1Ready = true;
            updateStatus();
        } else if (playerNumber == 2) {
            player2Ready = true;
            updateStatus();
        }
    }

    // 플레이어 연결 상태 업데이트
    private void updateStatus() {
        if (player1Ready && player2Ready) {
            statusLabel.setText("Both players are ready! Click to start.");
            startButton.setEnabled(true); // 플레이어가 모두 연결되면 게임 시작 버튼 활성화
        } else {
            statusLabel.setText("Waiting for players to connect...");
            startButton.setEnabled(false); // 연결되지 않은 경우 게임 시작 버튼 비활성화
        }
    }

    // 게임 시작 처리
    private void startGame() {
        // 게임 시작 로직 (예: 캐릭터 선택 화면으로 이동)
        JOptionPane.showMessageDialog(this, "Game is starting!");
        // 여기서 게임 로직을 추가 (예: 캐릭터 선택 화면으로 이동)
    }

    // 메인 메서드 예시
    public static void main(String[] args) {
        new GameStartUI();
    }
}
