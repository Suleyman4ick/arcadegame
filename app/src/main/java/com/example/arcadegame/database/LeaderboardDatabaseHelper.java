package com.example.arcadegame.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LeaderboardDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "leaderboard_database";
    private static final int DATABASE_VERSION = 2; // Увеличиваем версию базы данных

    private static final String TABLE_SCORES = "scores";
    private static final String COLUMN_SCORE = "score";

    public LeaderboardDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createScoresTable(db); // Создаем таблицу при создании базы данных
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Удаляем существующую таблицу при обновлении базы данных
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES);
            createScoresTable(db); // Создаем новую таблицу
        }
    }

    private void createScoresTable(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_SCORES + "("
                + COLUMN_SCORE + " INTEGER)";
        db.execSQL(createTableQuery);
    }

    public void resetDatabase() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES);
        createScoresTable(db);
        db.close();
    }

    public int getMaxScoreFromDatabase() {
        SQLiteDatabase db = getReadableDatabase();
        int maxScore = 0;

        Cursor cursor = db.query(TABLE_SCORES, new String[]{COLUMN_SCORE}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(COLUMN_SCORE);
            if (columnIndex != -1) {
                maxScore = cursor.getInt(columnIndex);
            }
        }
        cursor.close();

        return maxScore;
    }

    public void saveMaxScoreToDatabase(int maxScore) {
        SQLiteDatabase db = getWritableDatabase();

        // Clear the table before saving the new max score
        db.delete(TABLE_SCORES, null, null);

        // Insert the new max score
        ContentValues values = new ContentValues();
        values.put(COLUMN_SCORE, maxScore);
        db.insert(TABLE_SCORES, null, values);

        db.close();
    }
}

