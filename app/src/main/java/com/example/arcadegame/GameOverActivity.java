package com.example.arcadegame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameOverActivity extends AppCompatActivity {

    private int score;
    private int coins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Устанавливаем флаг FLAG_FULLSCREEN для активности
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game_over);

        // Получение данных из предыдущей активности
        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);
        coins = intent.getIntExtra("coins", 0);
        TextView coinsTextView = findViewById(R.id.coins_text);
        coinsTextView.setText("Монеты: " + coins);
        TextView scoreTextView = findViewById(R.id.score_text);
        scoreTextView.setText("Очки: " + score);

        PreferenceManager.saveHighScore(GameOverActivity.this, score);



        ImageButton homeButton = findViewById(R.id.home_button);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameOverIntent = new Intent(GameOverActivity.this, MainActivity.class);
                gameOverIntent.putExtra("coins", coins);
                startActivity(gameOverIntent);
                finish();
            }
        });

        ImageButton replayButton = findViewById(R.id.replay_button);
        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Обработка нажатия кнопки "Начать игру заново"
                startActivity(new Intent(GameOverActivity.this, GameActivity.class));
                finish();
            }
        });
    }
}