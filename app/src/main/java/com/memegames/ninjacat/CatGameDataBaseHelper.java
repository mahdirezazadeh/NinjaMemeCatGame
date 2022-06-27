package com.memegames.ninjacat;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CatGameDataBaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "catgame"; // the name of our database
    private static final int DB_VERSION = 1; // the version of the database

    public CatGameDataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        updateMyDatabase(sqLiteDatabase, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        updateMyDatabase(sqLiteDatabase, oldVersion, newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE USER (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "UserName TEXT, "
                    + "Password TEXT, "
                    + "IMAGE_RESOURCE_ID INTEGER);");

            db.execSQL("CREATE TABLE LEVEL (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "userId INTEGER, "
                    + "levelId INTEGER, "
                    + "score INTEGER);");
        }
    }
}
