package org.example.test;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;

class MyPanel extends JPanel {
	Image image; // 이미지 객체

	// 생성자에서 GIF 파일 로드
	MyPanel() {
		image = Toolkit.getDefaultToolkit().createImage("src/main/resources/animations/characters/moving.gif");
	}

	// 이미지를 그리기 위한 메서드
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g); // 부모 클래스의 paintComponent 호출
		if (image != null) {
			g.drawImage(image, 0, 0, this); // 이미지 그리기
		}
	}

	// 메인 메서드: MyPanel 실행
	public static void main(String[] args) {
		JFrame frame = new JFrame("GIF Test");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 400); // 프레임 크기 설정
		frame.setLocationRelativeTo(null); // 화면 중앙에 배치

		MyPanel panel = new MyPanel(); // MyPanel 인스턴스 생성
		frame.add(panel); // 프레임에 패널 추가
		frame.setVisible(true); // 프레임 표시
	}
}
