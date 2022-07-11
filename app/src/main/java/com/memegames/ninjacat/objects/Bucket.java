package com.memegames.ninjacat.objects;

public class Bucket {
    private boolean isEmpty;
    private int x;
    private int y;

    public Bucket(boolean isEmpty, int x, int y) {
        this.isEmpty = isEmpty;
        this.x = x;
        this.y = y;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
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
}
