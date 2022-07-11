package com.memegames.ninjacat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.logging.Level;

public class LevelsListActivity extends AppCompatActivity {

    String username = null;
    String[] textString;
    int[] drawableIds;
    int[] stars;
    CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levelslist);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        ImageButton btn_settings = (ImageButton) findViewById(R.id.settings);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
        }

        textString = new String[]{"Level 1", "Level 2", "Level 3", "Level 4", "Level 5", "Level 6", "Level 7", "Level 8", "Level 9", "Level 10", "Level 11"};
        stars = getScores(11);
        drawableIds = new int[]{R.drawable.level_1, R.drawable.level_2, R.drawable.level_3, R.drawable.level_4, R.drawable.level_5, R.drawable.level_6, R.drawable.level_7, R.drawable.level_8, R.drawable.level_9, R.drawable.level_10, R.drawable.level_11};

        adapter = new CustomAdapter(this, textString, drawableIds, stars);

        ListView listLevels = (ListView) findViewById(R.id.list_levels);
        listLevels.setAdapter(adapter);


        AdapterView.OnItemClickListener itemClickListener = (adapterView, view, position, id) -> {
            Intent intent = new Intent(LevelsListActivity.this, LevelActivity.class);
            intent.putExtra("level", id + 1);
            intent.putExtra("username", username);
            startActivity(intent);
        };
        listLevels.setOnItemClickListener(itemClickListener);

        btn_settings.setOnClickListener(view -> {
            Intent intent = new Intent(LevelsListActivity.this, ProfileActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        stars = getScores(11);
        adapter = new CustomAdapter(this, textString, drawableIds, stars);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        stars = getScores(11);
        adapter = new CustomAdapter(this, textString, drawableIds, stars);
    }

    private int[] getScores(int levels) {
        int[] picIds = new int[levels];
        for (int level = 0; level < levels; level++) {
            Cursor userScoreByLevel = CatGameDataBaseHelper.loadUserScoreByLevel(level + 1, this);
            Cursor cursor = CatGameDataBaseHelper.loadGameSettingsByLevel(level + 1, this);
            if (cursor.moveToFirst() && userScoreByLevel.moveToFirst()) {
                int maxScore = cursor.getInt(2);
                int userScore = userScoreByLevel.getInt(3);
                float rate = (float) userScore / maxScore;
                int rateNumber = Math.round(rate * 9);
                picIds[level] = loadCatsPicIdByRateNumber(rateNumber);
            } else
                picIds[level] = R.drawable.star0;
        }
        return picIds;
    }

    private int loadCatsPicIdByRateNumber(int rateNumber) {
        switch (rateNumber) {
            case 0:
                return R.drawable.star0;
            case 1:
                return R.drawable.star1;
            case 2:
                return R.drawable.star2;
            case 3:
                return R.drawable.star3;
            case 4:
                return R.drawable.star4;
            case 5:
                return R.drawable.star5;
            case 6:
                return R.drawable.star6;
            case 7:
                return R.drawable.star7;
            case 8:
                return R.drawable.star8;
            case 9:
                return R.drawable.star9;
            default:
                return R.drawable.star0;
        }
    }
}