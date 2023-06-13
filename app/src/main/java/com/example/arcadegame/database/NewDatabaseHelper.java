package com.example.arcadegame.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NewDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "new_database.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "collected_coins";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_COIN_COUNT = "coin_count";

    private static final String TABLE_COLLECTED_COINS = "collected_coins";


    public NewDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_COIN_COUNT + " INTEGER)";
        db.execSQL(createTableQuery);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COLLECTED_COINS);
        onCreate(db);
    }


    public void addCollectedCoinToDatabase(int coinCount) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COIN_COUNT, coinCount);
        db.insert(TABLE_NAME, null, values);
    }

    public int getTotalCoinsCountFromDatabase() {
        SQLiteDatabase db = getReadableDatabase();
        int totalCoinCount = 0;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT SUM(coin_count) FROM " + TABLE_COLLECTED_COINS, null);

            if (cursor != null && cursor.moveToFirst()) {
                totalCoinCount = cursor.getInt(0);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return totalCoinCount;
    }

}
