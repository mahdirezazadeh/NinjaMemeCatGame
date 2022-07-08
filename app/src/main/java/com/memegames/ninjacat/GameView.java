package com.memegames.ninjacat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends View {
    private final int numberOfColumns = 7;
    private final int numberOfRows = 8;
    int cellSize;
    int startHeight;
    boolean isOnMoving = false;
    int frameNumber = 0;
    int virusRadios = 0;
    private int framesNumber = 50;
    private long eachMoveMillis = 500;
    private int virusMargin = 40;
    private boolean isOnStart = true;
    private int blockMargin = 10;


    ArrayList<Virus> viruses = new ArrayList<>();
    ArrayList<Block> blocks = new ArrayList<>();


    @SuppressLint("DrawAllocation")
    Paint paint = new Paint();

    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, int level) {
        super(context);
        initBlocksValuesByLevel(level);
        initLowLevelVirusesValueByLevel(level);
        initHighLevelVirusesValueByLevel(level);
        frameNumber = framesNumber;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initializeBackground(canvas);


        if (frameNumber == framesNumber) {
            completeMovements();
            drawVirusesByXY(canvas);
            generateMoveViruses();
            frameNumber = 0;
        } else if (frameNumber < framesNumber) {
            moveViruses();
            drawVirusesByPixels(canvas);
            frameNumber++;
        }
        try {
            Thread.sleep(eachMoveMillis / framesNumber);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        invalidate();
    }

    private void moveViruses() {
        int movementPixels = cellSize / framesNumber;
        for (Virus virus : viruses) {
            moveVirus(virus, movementPixels);
        }
    }

    private void moveVirus(Virus virus, int movementPixels) {
        switch (virus.getCurrentMove()) {
            case UP:
                virus.moveUp(movementPixels);
                break;
            case LEFT:
                virus.moveLeft(movementPixels);
                break;
            case DOWN:
                virus.moveDown(movementPixels);
                break;
            case RIGHT:
                virus.moveRight(movementPixels);
                break;
            default:
                break;
        }
    }


    private void generateMoveViruses() {
        for (int i = 0; i < viruses.size(); i++) {
            generateMoveVirus(viruses.get(i));
        }
    }

    private void drawVirusesByPixels(Canvas canvas) {
        for (Virus virus : viruses) {
            paint.setColor(virus.getColor());
            canvas.drawCircle(virus.getPixelX(), virus.getPixelY(), virusRadios, paint);
        }

    }

    private void drawVirusesByXY(Canvas canvas) {
        virusRadios = getVirusRadios();

        for (int i = 0; i < viruses.size(); i++) {
            paint.setColor(viruses.get(i).getColor());
            canvas.drawCircle(getPixelVirusX(viruses.get(i).getX()), getPixelVirusY(viruses.get(i).getY()), virusRadios, paint);
            viruses.get(i).setPixelX(getPixelVirusX(viruses.get(i).getX()));
            viruses.get(i).setPixelY(getPixelVirusY(viruses.get(i).getY()));
        }
    }

    private void completeMovements() {
        for (Virus virus : viruses) {
            completeMovement(virus);
        }
    }

    private void completeMovement(Virus virus) {
        virus.setX(virus.getNewX());
        virus.setY(virus.getNewY());
        virus.setPixelX(getPixelVirusX(virus.getX()));
        virus.setPixelY(getPixelVirusY(virus.getY()));
    }

    private void generateMoveVirus(Virus virus) {
        int x = virus.getX();
        int y = virus.getY();
        int newX;
        int newY;
        do {
            newX = generateNewX(x);
            newY = generateNewY(y);
            if (newX - x != 0 && newY - y != 0) {
                boolean moveVertical = new Random().nextBoolean();
                if (moveVertical) {
                    newX = x;
                } else {
                    newY = y;
                }
            }
        } while (isNotValidXY(newX, newY));

        int moveX = newX - x;
        int moveY = newY - y;
        if (moveX != 0) {
            virus.setCurrentMove(moveX == 1 ? Movement.RIGHT : Movement.LEFT);
        } else if (moveY != 0) {
            virus.setCurrentMove(moveY == 1 ? Movement.DOWN : Movement.UP);
        } else {
            virus.setCurrentMove(Movement.NONE);
        }

        virus.setNewX(newX);
        virus.setNewY(newY);
    }

    private void initializeBackground(Canvas canvas) {
        int startColor = Color.parseColor("#3C4A58");
        int endColor = Color.parseColor("#0C0F12");

        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(12);

        cellSize = getWidth() / numberOfColumns;
        startHeight = getHeight() - (numberOfRows * cellSize);
        int round = (cellSize - blockMargin) / 5;

        for (int row = 0; row < numberOfRows; row++) {
            for (int col = 0; col < numberOfColumns; col++) {
                if (!isNotValidXY(col, row)) {
                    int startX = col * cellSize;
                    int endX = startX + cellSize;

                    int startY = startHeight + (row * (cellSize));
                    int endY = startY + cellSize;
                    paint.setShader(new LinearGradient(startX, startY, endX, endY, startColor, endColor, Shader.TileMode.MIRROR));
                    canvas.drawRoundRect(startX, startY, endX, endY, round, round, paint);
                }
            }
        }
        paint.setShader(null);

    }

    private boolean isNotValidXY(int newX, int newY) {
        long count = blocks.stream().filter(block -> block.getX() == newX && block.getY() == newY).count();
        return count > 0;
    }

    private ArrayList<Block> initBlocksValuesByLevel(int level) {
        int blocksNumber = 10;
        for (int i = 0; i < blocksNumber; i++) {
            int y;
            int x;
            do {
                x = generateRandomX();
                y = generateRandomY();
            } while (isNotValidXY(x, y));
            blocks.add(new Block(x, y));
        }
        return blocks;
    }

    private void initLowLevelVirusesValueByLevel(int level) {
        int virusesNumber = 10;
        for (int i = 0; i < virusesNumber; i++) {
            int y;
            int x;
            do {
                x = generateRandomX();
                y = generateRandomY();
            } while (isNotValidXY(x, y));
//            int color = Color.rgb(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
            int color = Color.rgb(0, 0, 255);
            Virus virus = new Virus(x, y, 10, color);
            viruses.add(virus);
        }
    }

    private void initHighLevelVirusesValueByLevel(int level) {
        int virusesNumber = 5;
        for (int i = 0; i < virusesNumber; i++) {
            int y;
            int x;
            do {
                x = generateRandomX();
                y = generateRandomY();
            } while (isNotValidXY(x, y));
//            int color = Color.rgb(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
            int color = Color.rgb(0, 255, 0);
            Virus virus = new Virus(x, y, 4, color);
            viruses.add(virus);
        }
    }

    private int generateRandomX() {
        return new Random().nextInt(numberOfColumns - 2) + 1;
    }

    private int generateRandomY() {
        return new Random().nextInt(numberOfRows - 2) + 1;
    }

    private int generateNewX(int x) {
        return Math.max(0, Math.min(x + randomMoveDiff(), numberOfColumns - 1));
    }

    private int generateNewY(int y) {
        return Math.max(0, Math.min(y + randomMoveDiff(), numberOfRows - 1));
    }

    private int randomMoveDiff() {
        return new Random().nextBoolean() ? 1 : -1;
    }

    private int getPixelVirusY(int y) {
        return startHeight + (y * cellSize) + virusRadios + virusMargin;
    }

    private int getPixelVirusX(int x) {
        return (x * cellSize) + virusRadios + virusMargin;
    }

    private int getVirusRadios() {
        return (cellSize / 2) - virusMargin;
    }

}
