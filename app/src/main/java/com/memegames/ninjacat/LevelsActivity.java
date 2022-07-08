package com.memegames.ninjacat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class LevelsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        setContentView(new GameView(this, 1));
    }
}