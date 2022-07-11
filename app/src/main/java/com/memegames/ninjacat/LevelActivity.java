package com.memegames.ninjacat;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LevelActivity extends AppCompatActivity {
    GameView game;

    int timeBySeconds;

    TextView timeView;
    TextView powerView;
    TextView scoreView;
    TextView UsernameView;
    TextView LevelIDView;
    LinearLayout gameView;
    private int startPower;
    private long level = 1;
    private String username = "username";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        setContentView(R.layout.activity_game);
        if (getIntent().getExtras() != null) {
            username = getIntent().getExtras().getString("username");
            level = getIntent().getExtras().getLong("level");
        }


        timeView = (TextView) findViewById(R.id.TimeView);

        powerView = (TextView) findViewById(R.id.powerView);
        scoreView = (TextView) findViewById(R.id.scoreView);
        UsernameView = (TextView) findViewById(R.id.GameUsernameView);
        LevelIDView = (TextView) findViewById(R.id.LevelID);

        gameView = (LinearLayout) findViewById(R.id.GameView);


        initValuesFromDatabase();
        game = new GameView(this, level);

        gameView.addView(game);

        initScoreBoard();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        game = null;
        finish();
    }

    private void initValuesFromDatabase() {
        Cursor cursor = CatGameDataBaseHelper.loadGameSettingsByLevel(level, this);
        cursor.moveToFirst();
        startPower = cursor.getInt(14);
        timeBySeconds = cursor.getInt(13);
    }

    private void initScoreBoard() {
        powerView.setText(String.format("Capacity: %d", startPower));
        scoreView.setText(String.format("Score: %d", 0));
        LevelIDView.setText(String.format("Level: %d", level));
        UsernameView.setText(String.format("Username: %s", username));

        startTimerCountDown(game, timeView);
    }

    private CountDownTimer startTimerCountDown(GameView game, TextView timeView) {
        CountDownTimer countDownTimer = new CountDownTimer(timeBySeconds * 1000L, 1000) {
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                timeView.setText(String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
            }

            public void onFinish() {
                if (game != null && game.isRunning())
                    game.winGame();
            }
        };
        countDownTimer.start();
        return countDownTimer;
    }


}