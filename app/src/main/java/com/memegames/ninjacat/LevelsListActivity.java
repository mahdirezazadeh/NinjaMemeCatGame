package com.memegames.ninjacat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.logging.Level;

public class LevelsListActivity extends AppCompatActivity {

    String username = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levelslist);
        getSupportActionBar().hide();

        ImageButton btn_settings = (ImageButton) findViewById(R.id.settings);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username");
        }

        String[] textString = {"Level 1", "Level 2", "Level 3", "Level 4", "Level 5", "Level 6", "Level 7", "Level 8", "Level 9", "Level 10", "Level 11"};
        int[] drawableIds = {R.drawable.level_1, R.drawable.level_2, R.drawable.level_3, R.drawable.level_4, R.drawable.level_5, R.drawable.level_6, R.drawable.level_7, R.drawable.level_8, R.drawable.level_9, R.drawable.level_10, R.drawable.level_11};
        int[] stars = {R.drawable.star9, R.drawable.star8, R.drawable.star7, R.drawable.star6, R.drawable.star5, R.drawable.star4, R.drawable.star3, R.drawable.star2, R.drawable.star1, R.drawable.star0, R.drawable.star0};

        CustomAdapter adapter = new CustomAdapter(this,  textString, drawableIds, stars);

        ListView listLevels = (ListView) findViewById(R.id.list_levels);
        listLevels.setAdapter(adapter);


        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(LevelsListActivity.this, LevelActivity.class);
                startActivity(intent);
            }
        };
        listLevels.setOnItemClickListener(itemClickListener);

        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LevelsListActivity.this, ProfileActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

    }
}