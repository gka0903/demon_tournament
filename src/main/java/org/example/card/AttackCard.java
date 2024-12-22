package org.example.card;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class AttackCard extends Card {
    // 생성자
    public AttackCard(CardData cardData) {
        super(cardData);
    }

    // 공격 범위 계산
    public List<Point> getGridPosList(Point playerCard) {
        switch (cardData.getAtkShape()) {
            case VERTICAL:
                return excludeOverRangeCoordList(playerCard, List.of(
                        new Point(0, 0),
                        new Point(0, 1)
                ));
            case HORIZONTAL:
                return excludeOverRangeCoordList(playerCard, List.of(
                        new Point(-1, 0),
                        new Point(0, 0),
                        new Point(1, 0)
                ));
            case SLASH:
                return excludeOverRangeCoordList(playerCard, List.of(
                        new Point(-1, -1),
                        new Point(0, 0),
                        new Point(1, 1)
                ));
            case BACKSLASH:
                return excludeOverRangeCoordList(playerCard, List.of(
                        new Point(-1, 1),
                        new Point(0, 0),
                        new Point(1, -1)
                ));
            case X:
                return excludeOverRangeCoordList(playerCard, List.of(
                        new Point(-1, 1),
                        new Point(0, 0),
                        new Point(1, -1),
                        new Point(-1, -1),
                        new Point(1, 1)
                ));
            case CROSS:
                return excludeOverRangeCoordList(playerCard, List.of(
                        new Point(-1, 0),
                        new Point(0, 0),
                        new Point(1, 0),
                        new Point(0, -1),
                        new Point(0, 1)
                ));
            case T_UP:
                return excludeOverRangeCoordList(playerCard, List.of(
                        new Point(-1, -1),
                        new Point(0, -1),
                        new Point(1, -1),
                        new Point(0, 0),
                        new Point(0, 1)
                ));
            case T_DOWN:
                return excludeOverRangeCoordList(playerCard, List.of(
                        new Point(-1, 1),
                        new Point(0, 1),
                        new Point(1, 1),
                        new Point(0, 0)
//                        new Point(0, -1)
                ));
            case H:
                return excludeOverRangeCoordList(playerCard, List.of(
                        new Point(-1, 1),
                        new Point(-1, 0),
                        new Point(-1, -1),
                        new Point(0, 0),
                        new Point(1, 1),
                        new Point(1, 0),
                        new Point(1, -1)
                ));
            case LYING_H:
                return excludeOverRangeCoordList(playerCard, List.of(
                        new Point(-1, 1),
                        new Point(0, 1),
                        new Point(1, 1),
                        new Point(0, 0),
                        new Point(-1, -1),
                        new Point(0, -1),
                        new Point(1, -1)
                ));
            default:
                throw new IllegalArgumentException("Unknown AttackShape: " + cardData.getAtkShape());
        }
    }

    // 좌표 유효성 검사
    private List<Point> excludeOverRangeCoordList(Point playerCoord, List<Point> coordList) {
        List<Point> newCoordList = new ArrayList<>();

        for (Point coord : coordList) {
            Point newCoord = new Point(playerCoord.x + coord.x, playerCoord.y + coord.y);
            if (newCoord.x >= 0 && newCoord.x <= 3 && newCoord.y >= 0 && newCoord.y <= 2) {
                newCoordList.add(newCoord);
            }
        }

        return newCoordList;
    }
}
