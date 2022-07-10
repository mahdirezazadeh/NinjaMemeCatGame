package com.memegames.ninjacat.objects;

import com.memegames.ninjacat.enums.Movement;

public class Player {
    private int x;
    private int y;
    private int power;
    private int score = 0;
    private Movement currentMove = Movement.NONE;

    public Player() {
    }

    public Player(int x, int y, int power) {
        this.x = x;
        this.y = y;
        this.power = power;
    }

    public Player(int x, int y, int newX, int newY, int pixelX, int pixelY, int power, Movement currentMove) {
        this.x = x;
        this.y = y;
        this.power = power;
        this.currentMove = currentMove;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public Movement getCurrentMove() {
        return currentMove;
    }

    public void setCurrentMove(Movement currentMove) {
        this.currentMove = currentMove;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void reducePower(int power) {
        this.power -= power;
    }

    public void addScore(int prize) {
        this.score += prize;
    }

    public void reduceScore(int scoreReductionAmount) {
        this.score -= scoreReductionAmount;
    }
}
