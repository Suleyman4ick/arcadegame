package com.example.arcadegame.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CoinDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "coin_database.db";
    private static final int DATABASE_VERSION = 2; // Увеличиваем версию базы данных

    private static final String TABLE_NAME = "coins";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_COIN_COUNT = "coin_count";
    private static final String COLUMN_DATE = "date";

    public CoinDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createCoinsTable(db); // Создаем таблицу при создании базы данных
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Удаляем существующую таблицу при обновлении базы данных
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            createCoinsTable(db); // Создаем новую таблицу
        }
    }

    private void createCoinsTable(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_COIN_COUNT + " INTEGER, " +
                COLUMN_DATE + " TEXT)";
        db.execSQL(createTableQuery);
    }

    public void resetDatabase() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        createCoinsTable(db);
        db.close();
    }

    public void addCoin(int coinCount) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_COIN_COUNT, coinCount);
        values.put(COLUMN_DATE, getCurrentDateTime());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public int getTotalCoinCount() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT SUM(" + COLUMN_COIN_COUNT + ") FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        int totalCoinCount = 0;
        if (cursor.moveToFirst()) {
            totalCoinCount = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return totalCoinCount;
    }

    public void updateCoinCount(int newCoinCount) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_COIN_COUNT, newCoinCount);

        // Обновляем запись с монетами
        db.update(TABLE_NAME, values, null, null);

        db.close();
    }

    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}

