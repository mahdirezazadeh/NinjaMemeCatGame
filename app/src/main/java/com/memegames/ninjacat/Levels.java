package com.memegames.ninjacat;

public class Levels {
    private String name;
    private int starsImageResourceId;
    private int iconResourceId;


    private Levels(String name, int starsImageResourceId, int iconResourceId) {
        this.name = name;
        this.starsImageResourceId = starsImageResourceId;
        this.iconResourceId = iconResourceId;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return this.name;
    }

    public int getStarsImageResourceId() {
        return starsImageResourceId;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }

    public static final Levels[] levels = {
            new Levels("Level 1", R.drawable.star0, R.drawable.level1),
            new Levels("Level 2", R.drawable.star0, R.drawable.level2),
            new Levels("Level 3", R.drawable.star0, R.drawable.level3)
    };
}
