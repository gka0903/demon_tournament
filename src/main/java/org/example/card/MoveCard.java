package org.example.card;

import java.awt.Point;

public class MoveCard extends Card {
    // 생성자
    public MoveCard(CardData cardData) {
        super(cardData);
    }

    // 이동 방향 출력 (디버깅용)
    public void printMoveDir() {
        System.out.println(cardData.getMoveDir().toString());
    }

    // 이동 방향에 따라 새로운 좌표 계산
    public Point getGridPosInfo(Point playerCoord) {
        switch (cardData.getMoveDir()) {
            case UP:
                return excludeOverRangeCoord(playerCoord, new Point(0, -1));
            case DOWN:
                return excludeOverRangeCoord(playerCoord, new Point(0, 1));
            case RIGHT:
                return excludeOverRangeCoord(playerCoord, new Point(1, 0));
            case LEFT:
                return excludeOverRangeCoord(playerCoord, new Point(-1, 0));
            default:
                throw new IllegalArgumentException("Unknown type of Direction: " + cardData.getMoveDir());
        }
    }

    // 범위를 초과한 좌표 처리
    private Point excludeOverRangeCoord(Point playerCoord, Point moveCoord) {
        Point newCoord = new Point(playerCoord.x + moveCoord.x, playerCoord.y + moveCoord.y);

        // 범위를 초과하면 (-2, -2) 반환
        if (newCoord.x < 0 || newCoord.x > 3 || newCoord.y < 0 || newCoord.y > 2) {
            return new Point(-2, -2);
        }

        return newCoord;
    }
}
