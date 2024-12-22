package org.example.connect;

import java.awt.Point;

public class GameManager {
    private int hp1 = 100, hp2 = 100, en1 = 100, en2 = 100;
    private Point p1, p2;

    public GameManager(int hp1, int hp2, int en1, int en2, Point p1, Point p2) {
        this.hp1 = hp1;
        this.hp2 = hp2;
        this.en1 = en1;
        this.en2 = en2;
        this.p1 = p1;
        this.p2 = p2;
    }

    public int getHp1() {
        return hp1;
    }

    public void setHp1(int hp1) {
        this.hp1 = hp1;
    }

    public int getHp2() {
        return hp2;
    }

    public void setHp2(int hp2) {
        this.hp2 = hp2;
    }

    public int getEn1() {
        return en1;
    }

    public void setEn1(int en1) {
        this.en1 = en1;
    }

    public int getEn2() {
        return en2;
    }

    public void setEn2(int en2) {
        this.en2 = en2;
    }

    public Point getP1() {
        return p1;
    }

    public void setP1(Point p1) {
        this.p1 = p1;
    }

    public Point getP2() {
        return p2;
    }

    public void setP2(Point p2) {
        this.p2 = p2;
    }
}
