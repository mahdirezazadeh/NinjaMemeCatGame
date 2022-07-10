package com.memegames.ninjacat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.widget.Toast;

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
                    + "IMAGE_RESOURCE BLOB);");

            db.execSQL("CREATE TABLE LEVEL (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "userId INTEGER, "
                    + "levelId INTEGER, "
                    + "score INTEGER);");
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
            return result.getCount() == 1;
        } catch (SQLiteException e){
            Toast toast = Toast.makeText(context, "Database unavailable!", Toast.LENGTH_SHORT);
            toast.show();
        }
        return username.equals("mahdi") && password.equals("1234");
    }
}
