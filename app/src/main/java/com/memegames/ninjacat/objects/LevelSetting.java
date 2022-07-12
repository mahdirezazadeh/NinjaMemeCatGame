package com.memegames.ninjacat.objects;


public class LevelSetting {
    private int levelNumber;
    private int columnsNumber;
    private int rowsNumber;
    private int blocksNumber;
    private int highLevelVirusCount;
    private int highLevelVirusPower;
    private int highLevelVirusPrize;
    private int lowLevelVirusCount;
    private int lowLevelVirusPower;
    private int lowLevelVirusPrize;
    private int scoreReductionAmount;
    private int gameTime;
    private int maxScore;
    private int playerStartPower;

    public LevelSetting(int levelNumber,
                        int columnsNumber,
                        int rowsNumber,
                        int blocksNumber,
                        int highLevelVirusCount,
                        int highLevelVirusPower,
                        int highLevelVirusPrize,
                        int lowLevelVirusCount,
                        int lowLevelVirusPower,
                        int lowLevelVirusPrize,
                        int scoreReductionAmount,
                        int gameTime) {
        this.levelNumber = levelNumber;
        this.columnsNumber = columnsNumber;
        this.rowsNumber = rowsNumber;
        this.blocksNumber = blocksNumber;
        this.highLevelVirusCount = highLevelVirusCount;
        this.highLevelVirusPower = highLevelVirusPower;
        this.highLevelVirusPrize = highLevelVirusPrize;
        this.lowLevelVirusCount = lowLevelVirusCount;
        this.lowLevelVirusPower = lowLevelVirusPower;
        this.lowLevelVirusPrize = lowLevelVirusPrize;
        this.scoreReductionAmount = scoreReductionAmount;
        this.gameTime = gameTime;
        this.playerStartPower = calculatePlayerStartPower(highLevelVirusCount, highLevelVirusPower, lowLevelVirusCount, lowLevelVirusPower);
        this.maxScore = highLevelVirusCount * highLevelVirusPrize + lowLevelVirusCount * lowLevelVirusPrize;
    }

    private int calculatePlayerStartPower(int highLevelVirusCount, int highLevelVirusPower, int lowLevelVirusCount, int lowLevelVirusPower) {
        int power = (highLevelVirusCount * highLevelVirusPower + lowLevelVirusCount * lowLevelVirusPower) / 2;
        if (power % 2 == 0)
            return power;
        return power + 1;
    }

    public int levelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public int columnsNumber() {
        return columnsNumber;
    }

    public void setColumnsNumber(int columnsNumber) {
        this.columnsNumber = columnsNumber;
    }

    public int rowsNumber() {
        return rowsNumber;
    }

    public void setRowsNumber(int rowsNumber) {
        this.rowsNumber = rowsNumber;
    }

    public int blocksNumber() {
        return blocksNumber;
    }

    public void setBlocksNumber(int blocksNumber) {
        this.blocksNumber = blocksNumber;
    }

    public int highLevelVirusCount() {
        return highLevelVirusCount;
    }

    public void setHighLevelVirusCount(int highLevelVirusCount) {
        this.highLevelVirusCount = highLevelVirusCount;
    }

    public int highLevelVirusPower() {
        return highLevelVirusPower;
    }

    public void setHighLevelVirusPower(int highLevelVirusPower) {
        this.highLevelVirusPower = highLevelVirusPower;
    }

    public int highLevelVirusPrize() {
        return highLevelVirusPrize;
    }

    public void setHighLevelVirusPrize(int highLevelVirusPrize) {
        this.highLevelVirusPrize = highLevelVirusPrize;
    }

    public int lowLevelVirusCount() {
        return lowLevelVirusCount;
    }

    public void setLowLevelVirusCount(int lowLevelVirusCount) {
        this.lowLevelVirusCount = lowLevelVirusCount;
    }

    public int lowLevelVirusPower() {
        return lowLevelVirusPower;
    }

    public void setLowLevelVirusPower(int lowLevelVirusPower) {
        this.lowLevelVirusPower = lowLevelVirusPower;
    }

    public int lowLevelVirusPrize() {
        return lowLevelVirusPrize;
    }

    public void setLowLevelVirusPrize(int lowLevelVirusPrize) {
        this.lowLevelVirusPrize = lowLevelVirusPrize;
    }

    public int scoreReductionAmount() {
        return scoreReductionAmount;
    }

    public void setScoreReductionAmount(int scoreReductionAmount) {
        this.scoreReductionAmount = scoreReductionAmount;
    }

    public int gameTime() {
        return gameTime;
    }

    public void setGameTime(int gameTime) {
        this.gameTime = gameTime;
    }

    public int playerStartPower() {
        return playerStartPower;
    }

    public void setPlayerStartPower(int playerStartPower) {
        this.playerStartPower = playerStartPower;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public int maxScore() {
        return maxScore;
    }
}
