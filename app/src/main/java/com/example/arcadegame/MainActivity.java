package com.example.arcadegame;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer gMusic;
    private int currencyCount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        gMusic = MediaPlayer.create(this, R.raw.game_music);
        gMusic.setLooping(true);
        gMusic.start();

        ImageButton playButton = findViewById(R.id.play_button);
        ImageButton soundButton = findViewById(R.id.sound_button);
        ImageButton musicButton = findViewById(R.id.music_button);
        ImageButton storeButton = findViewById(R.id.store_button);
        ImageButton leaderboardButton = findViewById(R.id.leaderboard_button);



        final ImageView backgroundOne = (ImageView) findViewById(R.id.background_one);
        final ImageView backgroundTwo = (ImageView) findViewById(R.id.background_two);

        final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(10000L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float width = backgroundOne.getWidth();
                final float translationX = width - (width * progress);
                backgroundOne.setTranslationX(translationX);
                backgroundTwo.setTranslationX(translationX - width);
            }
        });
        animator.start();

        findViewById(R.id.play_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GameActivity.class));
                gMusic.pause();
            }
        });

        musicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gMusic.isPlaying()) {
                    gMusic.pause();
                    musicButton.setImageResource(R.drawable.music_off3);
                } else {
                    gMusic.start();
                    musicButton.setImageResource(R.drawable.music);
                }
            }
        });

        storeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Получаем переданное количество монеток
                Intent intent = getIntent();
                int coins = intent.getIntExtra("coins", 0);
                // Открываем SkinShopActivity и передаем количество монеток
                Intent skinShopIntent = new Intent(MainActivity.this, SkinShopActivity.class);
                skinShopIntent.putExtra("coins", coins);
                startActivity(skinShopIntent);
            }
        });

        leaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получаем переданное значение score
                Intent intent = getIntent();
                int score = intent.getIntExtra("score", 0);
                // Открываем LeaderboardActivity и передаем значение score
                Intent leaderboardIntent = new Intent(MainActivity.this, LeaderboardActivity.class);
                leaderboardIntent.putExtra("score", score);
                startActivity(leaderboardIntent);
            }
        });
        soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gMusic != null) {
            gMusic.stop();
            gMusic.release();
            gMusic = null;
        }
    }
}

