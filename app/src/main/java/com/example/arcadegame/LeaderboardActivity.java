package com.example.arcadegame;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.arcadegame.database.LeaderboardDatabaseHelper;

public class LeaderboardActivity extends AppCompatActivity {
    private int highScore;
    private LeaderboardDatabaseHelper leaderboardDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.leaderboard_activity);

        ImageButton backButton = findViewById(R.id.back_button);
        TextView highScoreTextView = findViewById(R.id.textViewPlayer);
        // Создание экземпляра LeaderboardDatabaseHelper
        leaderboardDatabaseHelper = new LeaderboardDatabaseHelper(this);
        // Получение сохраненного наивысшего счета из базы данных
        int maxScore = leaderboardDatabaseHelper.getMaxScoreFromDatabase();
        float newSize = 20; // Размер шрифта, который вы хотите установить
        highScoreTextView.setTextSize(newSize);
        // Отображение наивысшего счета в TextView
        highScoreTextView.setText("Наивысший счет: " + maxScore);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // Метод для сохранения нового наивысшего счета в базе данных
    private void saveMaxScore(int newMaxScore) {
        leaderboardDatabaseHelper.saveMaxScoreToDatabase(newMaxScore);
    }
}


