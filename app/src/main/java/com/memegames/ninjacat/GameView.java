package com.memegames.ninjacat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GameView extends View {
    private static final int SCORE_REDUCTION_AMOUNT = 10;
    private final int numberOfColumns = 7;
    private final int numberOfRows = 8;
    int cellSize;
    int startHeight;
    boolean isOnMoving = false;
    int frameNumber = 0;
    int virusRadios = 0;
    private int framesNumber = 10;
    private long eachMoveMillis = 300;
    private int virusMargin = 40;
    private boolean isOnStart = true;
    private int blockMargin = 10;
    float actionDownX;
    float actionDownY;

    ArrayList<Virus> viruses = new ArrayList<>();
    ArrayList<Block> blocks = new ArrayList<>();
    private Player player;


    @SuppressLint("DrawAllocation")
    Paint paint = new Paint();
    private int playerMargin = 30;
    private boolean run = true;

    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, int level) {
        super(context);
        initBlocksValuesByLevel(level);
        initLowLevelVirusesValueByLevel(level);
        initHighLevelVirusesValueByLevel(level);
        frameNumber = framesNumber;
        initPlayer();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (viruses.size() > 0 && run) {
            super.onDraw(canvas);
            initializeBackground(canvas);
            drawPlayer(canvas);


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
        } else if (run) {
            winGame();
        }
    }

    private void drawPlayer(Canvas canvas) {
        Bitmap bitmap;
        if (player.getPower() < 2)
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.annoting_cat_full_100px);
        else
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.annoting_cat_close_100px);
        int bitmapSize = cellSize - playerMargin;
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmapSize, bitmapSize, false);

        int pixelVirusX = getPixelPlayerLeft(player.getX());
        int pixelVirusY = getPixelPlayerTop(player.getY());

        canvas.drawBitmap(bitmap, pixelVirusX, pixelVirusY, paint);

    }

    private int getPixelPlayerTop(int y) {
        return startHeight + (y * (cellSize)) + (playerMargin / 2);
    }

    private int getPixelPlayerLeft(int x) {
        return x * (cellSize) + (playerMargin / 2);
    }


    private void initPlayer() {
        int playerStartPower = 12;
        player = new Player(numberOfColumns - 1, numberOfRows - 2, playerStartPower);
    }

    private void moveViruses() {
        int movementPixels = cellSize / framesNumber;

        for (int virusIndex = 0; virusIndex < viruses.size(); virusIndex++) {
            virusIndex += moveVirus(viruses.get(virusIndex), movementPixels);
        }
    }

    private int moveVirus(Virus virus, int movementPixels) {
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
        if (isOnNextCoordinate()) {
            if (player.getX() == virus.getNewX() && player.getY() == virus.getNewY()) {
                return playerAndVirusMeet(virus) == MeetResult.VIRUS_REMOVED ? -1 : 0;
            }
        } else {
            if (player.getX() == virus.getX() && player.getY() == virus.getY()) {
                return playerAndVirusMeet(virus) == MeetResult.VIRUS_REMOVED ? -1 : 0;
            }
        }
        return 0;
    }


    private void generateMoveViruses() {
        for (int i = 0; i < viruses.size(); i++) {
            do {
                generateMoveVirus(viruses.get(i));
            } while (viruses.get(i).getCurrentMove() == Movement.NONE);
        }
    }

    private void drawVirusesByPixels(Canvas canvas) {
        for (Virus virus : viruses) {
            Bitmap bitmap = getVirusBitmap(virus.getPower());
            canvas.drawBitmap(bitmap, virus.getPixelX(), virus.getPixelY(), paint);
        }

    }

    @NonNull
    private Bitmap getVirusBitmap(int virusPower) {
        Bitmap bitmap;
        if (virusPower == 2)
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.knitting_ball_red_100px);
        else
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.knitting_ball_green_100px);
        int bitmapSize = cellSize - virusMargin;
        return Bitmap.createScaledBitmap(bitmap, bitmapSize, bitmapSize, false);
    }

    private void drawVirusesByXY(Canvas canvas) {
        virusRadios = getVirusRadios();
        for (Virus virus : viruses) {
            Bitmap bitmap = getVirusBitmap(virus.getPower());
            int pixelVirusX = getPixelVirusLeft(virus.getX());
            int pixelVirusY = getPixelVirusTop(virus.getY());

            canvas.drawBitmap(bitmap, pixelVirusX, pixelVirusY, paint);

            virus.setPixelX(pixelVirusX);
            virus.setPixelY(pixelVirusY);
        }
    }

    private int getPixelVirusTop(int y) {
        return startHeight + (y * (cellSize)) + (virusMargin / 2);
    }

    private int getPixelVirusLeft(int x) {
        return x * (cellSize) + (virusMargin / 2);
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
        long count = blocks.stream()
                .filter(block -> block.getX() == newX && block.getY() == newY)
                .count();
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
        int virusesNumber = 3;
        int lowLevelPower = 2;
        int lowLevelPrize = 10;
        for (int i = 0; i < virusesNumber; i++) {
            int y;
            int x;
            do {
                x = generateRandomX();
                y = generateRandomY();
            } while (isNotValidXY(x, y));
            Virus virus = new Virus(x, y, lowLevelPower);
            virus.setPrize(lowLevelPrize);
            viruses.add(virus);
        }
    }

    private void initHighLevelVirusesValueByLevel(int level) {
        int virusesNumber = 3;
        int highLevelPower = 4;
        int highLevelPrize = 20;
        for (int i = 0; i < virusesNumber; i++) {
            int y;
            int x;
            do {
                x = generateRandomX();
                y = generateRandomY();
            } while (isNotValidXY(x, y));
            Virus virus = new Virus(x, y, highLevelPower);
            virus.setPrize(highLevelPrize);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                actionDownX = event.getX();
                actionDownY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float x = event.getX() - actionDownX;
                float y = event.getY() - actionDownY;

                if (Math.abs(x) > Math.abs(y)) {
                    if (x < 0) {
                        movePlayerLeft();
                    } else if (x > 0) {
                        movePlayerRight();
                    }
                } else {
                    if (y < 0) {
                        movePlayerUp();
                    } else if (y > 0) {
                        movePlayerDown();
                    }
                }
                break;
            default:
                return false;
        }
        return true;
    }

    private void checkOccurrenceWithViruses() {
        List<Virus> removedViruses = getOccurrenceViruses();

        for (int virusIndex = 0; virusIndex < removedViruses.size(); virusIndex++) {
            Virus virus = removedViruses.get(virusIndex);
            playerAndVirusMeet(virus);
        }


    }

    private MeetResult playerAndVirusMeet(Virus virus) {
        if (player.getPower() >= virus.getPower()) {
            player.reducePower(virus.getPower());
            player.addScore(virus.getPrize());
            viruses.remove(virus);
            return MeetResult.VIRUS_REMOVED;
        } else if (player.getScore() >= SCORE_REDUCTION_AMOUNT) {
            player.reduceScore(SCORE_REDUCTION_AMOUNT);
            return MeetResult.NONE;
        } else {
            losePlayer();
            return MeetResult.PLAYER_LOST;
        }
    }

    private boolean isOnNextCoordinate() {
        return frameNumber > (framesNumber / 2);
    }

    @NonNull
    private List<Virus> getOccurrenceViruses() {
        if (isOnNextCoordinate())
            return viruses.stream().filter(virus -> virus.getNewX() == player.getX() && virus.getNewY() == player.getY())
                    .collect(Collectors.toList());
        return viruses.stream().filter(virus -> virus.getX() == player.getX() && virus.getY() == player.getY())
                .collect(Collectors.toList());

    }

    private void losePlayer() {
        run = false;
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        Activity activity = (Activity) getContext();
        alertDialog.setTitle("looser!!");
        alertDialog.setIcon(R.drawable.annoting_cat_full_100px);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(String.format("Score: %d", player.getScore()));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                (dialog, which) -> {
                    dialog.dismiss();
                    activity.finish();
                });
        alertDialog.show();
    }

    private void winGame() {
        Activity activity = (Activity) getContext();
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Successful");
        alertDialog.setIcon(R.drawable.annoting_cat_close_100px);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(String.format("Score: %d", player.getScore()));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                (dialog, which) -> {
                    dialog.dismiss();
                    activity.finish();
                });
        alertDialog.show();
    }

    private void movePlayerUp() {
        int newY = Math.max(player.getY() - 1, 0);
        if (!isNotValidXY(player.getX(), newY))
            player.setY(newY);
    }

    private void movePlayerRight() {
        int newX = Math.min(player.getX() + 1, numberOfColumns - 1);
        if (!isNotValidXY(newX, player.getY()))
            player.setX(newX);
    }

    private void movePlayerLeft() {
        int newX = Math.max(player.getX() - 1, 0);
        if (!isNotValidXY(newX, player.getY()))
            player.setX(newX);
    }

    public void movePlayerDown() {
        int newY = Math.min(player.getY() + 1, numberOfRows - 1);
        if (!isNotValidXY(player.getX(), newY))
            player.setY(newY);
        checkOccurrenceWithViruses();
    }
}
