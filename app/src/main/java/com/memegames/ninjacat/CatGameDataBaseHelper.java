package com.memegames.ninjacat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Toast;

import com.memegames.ninjacat.objects.LevelSetting;

import java.util.ArrayList;
import java.util.List;

public class CatGameDataBaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "catgame"; // the name of our database
    private static final int DB_VERSION = 2; // the version of the database

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
                    + "IMAGE_RESOURCE BLOB,"
                    + "LOG_IN INTEGER);");

            db.execSQL("CREATE TABLE LEVEL (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "levelNumber INTEGER, "
                    + "maxScore INTEGER, "
                    + "cloumnsNumber INTEGER, "
                    + "rowsNumber INTEGER, "
                    + "blocksNumber INTEGER, "
                    + "highLevelVirusCount INTEGER, "
                    + "highLevelVirusPower INTEGER, "
                    + "highLevelVirusPrize INTEGER, "
                    + "lowLevelVirusCount INTEGER, "
                    + "lowLevelVirusPower INTEGER, "
                    + "lowLevelVirusPrize INTEGER, "
                    + "scoreReductionAmount INTEGER, "
                    + "gameTime INTEGER, "
                    + "playerStartPower INTEGER);");

            db.execSQL("CREATE TABLE LEVEL_USER (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "userId INTEGER, "
                    + "levelId INTEGER, "
                    + "score INTEGER);");

            ArrayList<LevelSetting> levels = new ArrayList<>();
            levels.add(new LevelSetting(1,
                    5,
                    7,
                    5,
                    1,
                    4,
                    20,
                    3,
                    2,
                    10,
                    10,
                    120,
                    12,
                    50));

            levels.add(new LevelSetting(2, 5, 8, 8, 2, 4, 20, 4, 2, 10, 10, 100, 12, 80));

            levels.add(new LevelSetting(3, 6, 8, 10, 3, 4, 20, 4, 2, 10, 10, 100, 12, 100));

            levels.add(new LevelSetting(4, 10, 8, 10, 4, 4, 20, 4, 2, 10, 10, 100, 12, 100));

            levels.add(new LevelSetting(5, 10, 3, 5, 3, 4, 20, 4, 2, 10, 10, 100, 12, 100));

            insert(levels, db);
        }
        if (oldVersion < 2) {
            ArrayList<LevelSetting> levels = new ArrayList<>();

            levels.add(new LevelSetting(6, 8, 10, 14, 5, 4, 20, 7, 2, 10, 10, 100, 18, 170));

            levels.add(new LevelSetting(7, 7, 7, 10, 5, 4, 20, 7, 2, 10, 10, 60, 18, 170));

            levels.add(new LevelSetting(8, 9, 7, 15, 5, 4, 20, 7, 2, 10, 10, 60, 18, 170));

            insert(levels, db);
        }
    }


    public static void signup(String username, String password, byte[] image, Context context) {
        SQLiteOpenHelper sqLiteOpenHelper = new CatGameDataBaseHelper(context);
        try {
            ContentValues userValues = new ContentValues();
            userValues.put("username", username);
            userValues.put("password", password);
            userValues.put("image_resource", image);
            SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
            db.insert("User", null, userValues);
        } catch (SQLiteException e) {
            Toast.makeText(context, "Database is NOT available!", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean checkUsernameConstraint(String username, Context context) {
        SQLiteOpenHelper sqLiteOpenHelper = new CatGameDataBaseHelper(context);
        try {
            SQLiteDatabase readableDatabase = sqLiteOpenHelper.getReadableDatabase();
            Cursor result = readableDatabase.rawQuery(
                    "SELECT * FROM USER WHERE Username = ?",
                    new String[]{username});
            return result.getCount() == 0;
        } catch (SQLiteException e) {
            Toast.makeText(context, "Database is NOT available!", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


    public static boolean authenticate(String username, String password, Context context) {
        SQLiteOpenHelper sqLiteOpenHelper = new CatGameDataBaseHelper(context);
        try {
            SQLiteDatabase readableDatabase = sqLiteOpenHelper.getReadableDatabase();
            Cursor result = readableDatabase.rawQuery(
                    "SELECT * FROM USER WHERE Username = ? AND Password = ?",
                    new String[]{username, password});
            String x = String.valueOf(result.getCount());
            boolean res = result.getCount() == 1;
            result.close();
            return res;
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(context, "Database unavailable!", Toast.LENGTH_SHORT);
            toast.show();
        }
        return false;
    }

    public static Cursor loadGameSettingsByLevel(long level, Context context) {
        SQLiteOpenHelper sqLiteOpenHelper = new CatGameDataBaseHelper(context);
        try {
            SQLiteDatabase readableDatabase = sqLiteOpenHelper.getReadableDatabase();
            Cursor result = readableDatabase.rawQuery(
                    "SELECT * FROM LEVEL WHERE levelNumber = ?",
                    new String[]{String.valueOf(level)});
            return result;
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(context, "Database unavailable!", Toast.LENGTH_SHORT);
            toast.show();
        }
        return null;
    }

    private void insert(List<LevelSetting> levels, SQLiteDatabase db) {

        for (LevelSetting l : levels) {

            db.execSQL("INSERT INTO LEVEL " +
                    "(levelNumber, cloumnsNumber, rowsNumber, blocksNumber, highLevelVirusCount, highLevelVirusPower, highLevelVirusPrize, lowLevelVirusCount, lowLevelVirusPower, lowLevelVirusPrize, maxScore, scoreReductionAmount, gameTime, playerStartPower) VALUES" +
                    "(" + l.levelNumber() + ", " + l.columnsNumber() + ", " + l.rowsNumber() + ", " + l.blocksNumber() + ", " + l.highLevelVirusCount() + ", " + l.highLevelVirusPower() + ", " + l.highLevelVirusPrize() + ", " + l.lowLevelVirusCount() + ", " + l.lowLevelVirusPower() + ", " + l.lowLevelVirusPrize() + ", " + l.maxScore() + ", " + l.scoreReductionAmount() + ", " + l.gameTime() + ", " + l.playerStartPower() + ");");
        }
    }

    public static void saveLogin(String username, Context context) {
        SQLiteOpenHelper sqLiteOpenHelper = new CatGameDataBaseHelper(context);
        try {
            SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
            ContentValues userValues = new ContentValues();
            userValues.put("LOG_IN", 1);
            db.update("USER", userValues, "UserName = ?", new String[]{username});
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(context, "Database unavailable!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public static void invalidateLogin(Context context) {
        SQLiteOpenHelper sqLiteOpenHelper = new CatGameDataBaseHelper(context);
        try {
            SQLiteDatabase db = sqLiteOpenHelper.getWritableDatabase();
            ContentValues userValues = new ContentValues();
            userValues.put("LOG_IN", 0);
            db.update("USER", userValues, "LOG_IN = ?", new String[]{"1"});
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(context, "Database unavailable!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public static String getLoggedInUser(Context context) {
        SQLiteOpenHelper sqLiteOpenHelper = new CatGameDataBaseHelper(context);
        try {
            SQLiteDatabase readableDatabase = sqLiteOpenHelper.getReadableDatabase();
            Cursor result = readableDatabase.rawQuery(
                    "SELECT * FROM USER WHERE LOG_IN = 1",
                    new String[]{});

            if (result.moveToNext()) {
                String username = result.getString(1);
                result.close();
                return username;
            }
            return null;
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(context, "Database unavailable!", Toast.LENGTH_SHORT);
            toast.show();
        }
        return null;
    }
}
