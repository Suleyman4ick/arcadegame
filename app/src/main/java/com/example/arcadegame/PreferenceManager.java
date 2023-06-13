package com.example.arcadegame;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private static final String PREFERENCE_NAME = "MyPreferences";
    private static final String KEY_HIGH_SCORE = "HighScore";

    public static void saveHighScore(Context context, int highScore) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_HIGH_SCORE, highScore);
        editor.apply();
    }

    public static int getHighScore(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_HIGH_SCORE, 0);
    }
}