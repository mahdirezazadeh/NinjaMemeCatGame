package com.memegames.ninjacat;

public class Virus {
    private int x;
    private int y;
    private int newX;
    private int newY;
    private int pixelX;
    private int pixelY;
    private int power;
    private int prize;
    private Movement currentMove;

    public Virus() {
    }

    public Virus(int x, int y, int power) {
        this.x = x;
        this.y = y;
        this.newX = x;
        this.newY = y;
        this.power = power;
    }


    public void moveUp(int movementPixels) {
        pixelY = pixelY - movementPixels;
    }

    public void moveDown(int movementPixels) {
        pixelY = pixelY + movementPixels;
    }

    public void moveLeft(int movementPixels) {
        pixelX = pixelX - movementPixels;
    }

    public void moveRight(int movementPixels) {
        pixelX = pixelX + movementPixels;
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

    public int getNewX() {
        return newX;
    }

    public void setNewX(int newX) {
        this.newX = newX;
    }

    public int getNewY() {
        return newY;
    }

    public void setNewY(int newY) {
        this.newY = newY;
    }

    public Movement getCurrentMove() {
        return currentMove;
    }

    public void setCurrentMove(Movement currentMove) {
        this.currentMove = currentMove;
    }

    public int getPixelX() {
        return pixelX;
    }

    public void setPixelX(int pixelX) {
        this.pixelX = pixelX;
    }

    public int getPixelY() {
        return pixelY;
    }

    public void setPixelY(int pixelY) {
        this.pixelY = pixelY;
    }

    public int getPrize() {
        return prize;
    }

    public void setPrize(int prize) {
        this.prize = prize;
    }
}
