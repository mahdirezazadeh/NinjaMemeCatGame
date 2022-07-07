package com.memegames.ninjacat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.logging.Level;

public class LevelsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levelslist);

        String[] textString = {"Level 1", "Level 2", "Level 3"};
        int[] drawableIds = {R.drawable.level1, R.drawable.level2, R.drawable.level3};
        int[] stars = {R.drawable.star5, R.drawable.star2, R.drawable.star0};

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

    }
}