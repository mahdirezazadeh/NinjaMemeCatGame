package com.memegames.ninjacat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.memegames.ninjacat.enums.MeetResult;
import com.memegames.ninjacat.enums.Movement;
import com.memegames.ninjacat.objects.Block;
import com.memegames.ninjacat.objects.Bucket;
import com.memegames.ninjacat.objects.Player;
import com.memegames.ninjacat.objects.Virus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GameView extends View {
    private static final long EACH_MOVE_MILLIS = 500;
    private static final int FRAMES_NUMBER = 10;
    private static final int BLOCK_MARGIN = 10;
    private static final int VIRUS_MARGIN = 40;
    private static final int PLAYER_MARGIN = 30;

    private int level;

    private int numberOfColumns = 7;
    private int numberOfRows = 8;
    private int blocksNumber = 10;
    private int playerStartPower = 12;
    private int lowLevelVirusesNumber = 3;
    private int lowLevelPower = 2;
    private int lowLevelPrize = 10;
    private int highLevelVirusesNumber = 3;
    private int highLevelPower = 4;
    private int highLevelPrize = 20;
    private int scoreReductionAmount = 10;

    private int cellSize;
    private int startHeight;
    private int startWidth;
    private int frameNumber = 0;
    private float actionDownX;
    private float actionDownY;
    private boolean run = true;
    private float leftTimeBySeconds;

    @SuppressLint("DrawAllocation")
    Paint paint = new Paint();

    ArrayList<Virus> viruses = new ArrayList<>();
    ArrayList<Block> blocks = new ArrayList<>();
    private Player player;
    private Bucket bucket;


    public GameView(Context context) {
        super(context);
        level = 1;
    }

    public GameView(Context context, int level) {
        super(context);
        this.level = level;
        initValuesByLevel();
        initBlocksValuesByLevel();
        initLowLevelVirusesValueByLevel();
        initHighLevelVirusesValueByLevel();
        frameNumber = FRAMES_NUMBER;
        initPlayer();
        initBucket();

    }

    private void initBucket() {
        bucket = new Bucket(false, numberOfColumns - 1, numberOfRows - 1);
    }

    private void initValuesByLevel() {
        Cursor cursor = CatGameDataBaseHelper.loadGameSettingsByLevel(level, getContext());
        cursor.moveToFirst();

        numberOfColumns = cursor.getInt(3);
        numberOfRows = cursor.getInt(4);
        blocksNumber = cursor.getInt(5);
        highLevelVirusesNumber = cursor.getInt(6);
        highLevelPower = cursor.getInt(7);
        highLevelPrize = cursor.getInt(8);
        lowLevelVirusesNumber = cursor.getInt(9);
        lowLevelPower = cursor.getInt(10);
        lowLevelPrize = cursor.getInt(11);
        scoreReductionAmount = cursor.getInt(12);
        playerStartPower = cursor.getInt(14);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (viruses.size() > 0 && run) {
            super.onDraw(canvas);
            initializeBackground(canvas);
            drawBucket(canvas);
            drawPlayer(canvas);

            updateScoreBoard();


            if (frameNumber == FRAMES_NUMBER) {
                completeMovements();
                drawVirusesByXY(canvas);
                generateMoveViruses();

                frameNumber = 0;
            } else if (frameNumber < FRAMES_NUMBER) {
                moveViruses();
                drawVirusesByPixels(canvas);
                frameNumber++;
            }
            try {
                Thread.sleep(EACH_MOVE_MILLIS / FRAMES_NUMBER);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            invalidate();
        } else if (run) {
            winGame();
        }
    }

    private void drawBucket(Canvas canvas) {
        Bitmap bitmap;
        if (bucket.isEmpty())
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.emptypower);
        else
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.newpowr);
        int bitmapSize = cellSize - PLAYER_MARGIN;
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmapSize, bitmapSize, false);

        int pixelVirusX = getPixelPlayerLeft(bucket.getX());
        int pixelVirusY = getPixelPlayerTop(bucket.getY());

        canvas.drawBitmap(bitmap, pixelVirusX, pixelVirusY, paint);
    }

    private void updateScoreBoard() {
        TextView powerView = (TextView) getRootView().findViewById(R.id.powerView);
        powerView.setText(String.format("Capacity: %d", player.getPower()));
        TextView scoreView = (TextView) getRootView().findViewById(R.id.scoreView);
        scoreView.setText(String.format("Score: %d", player.getScore()));
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
                checkOccurrenceWithViruses();
                isOnBucket();
                break;
            default:
                return false;
        }
        return true;
    }

    //    init game
    private void initPlayer() {
        player = new Player(numberOfColumns - 1, numberOfRows - 2, playerStartPower);
    }

    private void initializeBackground(Canvas canvas) {
        int startColor = Color.parseColor("#3C4A58");
        int endColor = Color.parseColor("#0C0F12");
        paint.setColor(Color.parseColor("#AFAFAF"));

        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(12);

//        getHeight();

        int height = getHeight() - getHeight() / 10;
        int width = getWidth() - (startWidth * 2);


        int rowCellSize = getHeight() / numberOfRows;
        int colCellSize = getWidth() / numberOfColumns;

        if (rowCellSize > colCellSize) {
            cellSize = colCellSize;
            startHeight = (getHeight() - (numberOfRows * cellSize)) / 2;
            startWidth = getWidth() - (numberOfColumns * cellSize);
            width = getWidth();

        } else {
            cellSize = rowCellSize;
            startWidth = (getWidth() - (numberOfColumns * cellSize)) / 2;
            startHeight = getHeight() - (numberOfRows * cellSize);
            height = getHeight();
        }

//        startHeight = height - (numberOfRows * cellSize);

        int round = (cellSize - BLOCK_MARGIN) / 5;

        for (int row = 0; row < numberOfRows; row++) {
            for (int col = 0; col < numberOfColumns; col++) {
                if (!isNotValidXY(col, row)) {
                    int startX = startWidth + col * cellSize;
                    int endX = startX + cellSize;

                    int startY = startHeight + (row * (cellSize));
                    int endY = startY + cellSize;
                    paint.setShader(new LinearGradient(startX, startY, endX, endY, startColor, endColor, Shader.TileMode.MIRROR));
                    canvas.drawRoundRect(startX, startY, endX, endY, round, round, paint);
                }
            }
        }
        paint.setShader(null);

        initScoreBoard(canvas);

    }

    private void initScoreBoard(Canvas canvas) {
        int score = player.getScore();
    }

    private void initBlocksValuesByLevel() {
        for (int i = 0; i < blocksNumber; i++) {
            int y;
            int x;
            do {
                x = generateRandomX();
                y = generateRandomY();
            } while (isNotValidXY(x, y));
            blocks.add(new Block(x, y));
        }
    }

    private void initLowLevelVirusesValueByLevel() {
        for (int i = 0; i < lowLevelVirusesNumber; i++) {
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

    private void initHighLevelVirusesValueByLevel() {
        for (int i = 0; i < highLevelVirusesNumber; i++) {
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

    //    draw virus
    private void drawVirusesByPixels(Canvas canvas) {
        for (Virus virus : viruses) {
            Bitmap bitmap = getVirusBitmap(virus.getPower());
            canvas.drawBitmap(bitmap, virus.getPixelX(), virus.getPixelY(), paint);
        }

    }

    private void drawVirusesByXY(Canvas canvas) {
        for (Virus virus : viruses) {
            Bitmap bitmap = getVirusBitmap(virus.getPower());
            int pixelVirusX = getPixelVirusLeft(virus.getX());
            int pixelVirusY = getPixelVirusTop(virus.getY());

            canvas.drawBitmap(bitmap, pixelVirusX, pixelVirusY, paint);

            virus.setPixelX(pixelVirusX);
            virus.setPixelY(pixelVirusY);
        }
    }

    //    move virus
    private void moveViruses() {
        int movementPixels = cellSize / FRAMES_NUMBER;

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

    //    generate move virus
    private void generateMoveViruses() {
        for (int i = 0; i < viruses.size(); i++) {
            do {
                generateMoveVirus(viruses.get(i));
            } while (viruses.get(i).getCurrentMove() == Movement.NONE);
        }
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

    //    complete movements of virus
    private void completeMovements() {
        for (Virus virus : viruses) {
            completeMovement(virus);
        }
    }

    private void completeMovement(Virus virus) {
        virus.setX(virus.getNewX());
        virus.setY(virus.getNewY());
        virus.setPixelX(getPixelVirusLeft(virus.getX()));
        virus.setPixelY(getPixelVirusTop(virus.getY()));
    }

    //    load pixel of virus
    private int getPixelVirusTop(int y) {
        return startHeight + (y * (cellSize)) + (VIRUS_MARGIN / 2);
    }

    private int getPixelVirusLeft(int x) {
        return x * (cellSize) + (VIRUS_MARGIN / 2) + startWidth;
    }

    //    load virus bitmap
    private Bitmap getVirusBitmap(int virusPower) {
        Bitmap bitmap;
        if (virusPower == 2)
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.knitting_ball_red_100px);
        else
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.knitting_ball_green_100px);
        int bitmapSize = cellSize - VIRUS_MARGIN;
        return Bitmap.createScaledBitmap(bitmap, bitmapSize, bitmapSize, false);
    }


    //    draw player
    private void drawPlayer(Canvas canvas) {
        Bitmap bitmap;
        if (player.getPower() < 2)
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.annoting_cat_full_100px);
        else
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.annoting_cat_close_100px);
        int bitmapSize = cellSize - PLAYER_MARGIN;
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmapSize, bitmapSize, false);

        int pixelVirusX = getPixelPlayerLeft(player.getX());
        int pixelVirusY = getPixelPlayerTop(player.getY());

        canvas.drawBitmap(bitmap, pixelVirusX, pixelVirusY, paint);

    }

    //    lose or win player
    private void losePlayer() {
        run = false;
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        Activity activity = (Activity) getContext();
        alertDialog.setTitle("YOU LOST!");
        alertDialog.setIcon(R.drawable.annoting_cat_full_100px);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(String.format("DON’T HURT THE CAT!\nScore: %d", player.getScore()));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Sorry",
                (dialog, which) -> {
                    dialog.dismiss();
                    activity.finish();
                });
        alertDialog.show();
    }

    public void winGame() {
        Activity activity = (Activity) getContext();
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("YOU DID IT");
        alertDialog.setIcon(R.drawable.annoting_cat_close_100px);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(String.format("Score: %d", player.getScore()));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Bring on the next",
                (dialog, which) -> {
                    dialog.dismiss();
                    activity.finish();
                });
        alertDialog.show();
    }

    //    move player
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
    }

    private void isOnBucket() {
        if (!bucket.isEmpty() && player.getX() == numberOfColumns - 1 && player.getY() == numberOfRows - 1) {
            bucket.setEmpty(true);
            player.setPower(playerStartPower);
        }
    }

    //    load pixel of player
    private int getPixelPlayerTop(int y) {
        return startHeight + (y * (cellSize)) + (PLAYER_MARGIN / 2);
    }

    private int getPixelPlayerLeft(int x) {
        return x * (cellSize) + (PLAYER_MARGIN / 2) + startWidth;
    }


    //    virus and player
    private void checkOccurrenceWithViruses() {
        List<Virus> removedViruses = getOccurrenceViruses();

        for (int virusIndex = 0; virusIndex < removedViruses.size(); virusIndex++) {
            Virus virus = removedViruses.get(virusIndex);
            playerAndVirusMeet(virus);
        }


    }

    private List<Virus> getOccurrenceViruses() {
        if (isOnNextCoordinate())
            return viruses.stream().filter(virus -> virus.getNewX() == player.getX() && virus.getNewY() == player.getY())
                    .collect(Collectors.toList());
        return viruses.stream().filter(virus -> virus.getX() == player.getX() && virus.getY() == player.getY())
                .collect(Collectors.toList());

    }

    private MeetResult playerAndVirusMeet(Virus virus) {
        if (player.getPower() >= virus.getPower()) {
            player.reducePower(virus.getPower());
            player.addScore(virus.getPrize());
            viruses.remove(virus);
            return MeetResult.VIRUS_REMOVED;
        } else if (player.getScore() >= scoreReductionAmount && !virus.getJustMeetPlayer()) {
            virus.setJustMeetPlayer(true);
            player.reduceScore(scoreReductionAmount);

            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(EACH_MOVE_MILLIS * 2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    virus.setJustMeetPlayer(false);
                }
            });
            thread.start();


            return MeetResult.NONE;
        } else if (!virus.getJustMeetPlayer()) {
            losePlayer();
            return MeetResult.PLAYER_LOST;
        }
        return MeetResult.NONE;
    }

    private boolean isOnNextCoordinate() {
        return frameNumber > (FRAMES_NUMBER / 2);
    }

    private boolean isNotValidXY(int newX, int newY) {
        long count = blocks.stream()
                .filter(block -> block.getX() == newX && block.getY() == newY)
                .count();
        return count > 0;
    }


    //    generate randoms
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

    public boolean isRunning() {
        return run;
    }
}
