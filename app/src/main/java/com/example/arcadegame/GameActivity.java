package com.example.arcadegame;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.arcadegame.database.CoinDatabaseHelper;
import com.example.arcadegame.database.LeaderboardDatabaseHelper;


public class GameActivity extends AppCompatActivity {
    private ImageView characterImageView;
    private ImageView obstacleImageView;
    private ImageView obstacle2ImageView;
    private ImageView coinImageView;
    private TextView scoreTextView;
    private TextView coinsTextView;
    private int score = 0;
    private int obstacleSpeed = 1;
    private int obstacle2Speed = 1;
    private boolean isGameRunning = false;
    private boolean coinCollected;
    int REQUEST_CODE = 1;

    private Handler handler = new Handler();
    private Runnable gameRunnable = new Runnable() {
        @Override
        public void run() {
            if (isGameRunning) {
                updateGame();
                handler.postDelayed(this, 10);
            }
        }
    };

    private Rect characterRect;
    private Rect obstacleRect;
    private boolean isObstacleVisible = false;
    private boolean isObstacle2Visible = false;
    private boolean isJumping = false;
    private int jumpHeight = 440;
    private int jumpDuration = 1400;
    private ImageView backgroundImageView;
    private ImageView backgroundtheImageView;
    private LeaderboardDatabaseHelper leaderboardDatabaseHelper;
    private int obstacleDelay = 2000; // Задержка между появлением препятствий (в миллисекундах)
    private boolean isObstacle1Visible = false; // Флаг видимости первого препятствия
    private int obstacle2Delay = 2000; // Задержка для второго препятствия в миллисекундах

    private int selectedSkin = 0; // Добавляем переменную для отслеживания выбранного скина

    private int equippedSkinId = 0;
    private MediaPlayer mediaPlayer;


    int SKIN_1_ID = 1;
    int SKIN_2_ID = 2;
    int SKIN_3_ID = 3;
    int SKIN_4_ID = 4;
    int SKIN_5_ID = 0;

    private int coins = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game);
        characterImageView = findViewById(R.id.character);
        obstacleImageView = findViewById(R.id.obstacle);
        coinImageView = findViewById(R.id.coin);
        scoreTextView = findViewById(R.id.score_text);
        coinsTextView = findViewById(R.id.coins_text);
        obstacle2ImageView = findViewById(R.id.obstacle2);
        backgroundImageView =findViewById(R.id.background_two);
        backgroundtheImageView = findViewById(R.id.background_one);
        leaderboardDatabaseHelper = new LeaderboardDatabaseHelper(this);

        mediaPlayer = MediaPlayer.create(this, R.raw.sound_coin);


        final ImageView backgroundOne = (ImageView) findViewById(R.id.background_one);
        final ImageView backgroundTwo = (ImageView) findViewById(R.id.background_two);

        // Получение объекта SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        boolean isSkin1Purchased = sharedPreferences.getBoolean("skin1Purchased", false);
        boolean isSkin2Purchased = sharedPreferences.getBoolean("skin2Purchased", false);
        boolean isSkin3Purchased = sharedPreferences.getBoolean("skin3Purchased", false);
        boolean isSkin4Purchased = sharedPreferences.getBoolean("skin4Purchased", false);
        boolean isSkin5Purchased = sharedPreferences.getBoolean("skin5Purchased", false);
        int equippedSkinId = sharedPreferences.getInt("equippedSkinId", 0);


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

        backgroundtheImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    jumpCharacter();
                }
                return true;
            }
        });

        backgroundImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    jumpCharacter();
                }
                return true;
            }
        });


        AnimationDrawable runAnimation;
        if (equippedSkinId == SKIN_5_ID) {
            runAnimation = loadDefaultRunAnimation();

        } else if (equippedSkinId == SKIN_1_ID) {
            runAnimation = loadRunAnimationSkin1();

        } else if (equippedSkinId == SKIN_2_ID){
            runAnimation = loadRunAnimationSkin2();

        }else if (equippedSkinId == SKIN_3_ID){
            runAnimation = loadRunAnimationSkin3();
        }
        else {
            runAnimation = loadRunAnimationSkin4();
        }
        characterImageView.setImageDrawable(runAnimation);
        runAnimation.start();
        startGame();
    }

    private void startGame() {
        score = 0;
        coins = 0;
        obstacleSpeed = 5;
        obstacle2Speed = 5;
        scoreTextView.setText("Очки: " + score);
        coinsTextView.setText("Монеты: " + coins);

        isObstacleVisible = false;
        isObstacle2Visible = false;


        ViewGroup.LayoutParams coinParams = coinImageView.getLayoutParams();
        coinParams.width = 500;
        coinParams.height = 500;
        coinImageView.setLayoutParams(coinParams);
        coinImageView.setX(-coinImageView.getWidth());
        characterImageView.setX(0);
        isJumping = false;
        isGameRunning = true;
        handler.removeCallbacks(gameRunnable);
        handler.post(gameRunnable);
        coinsTextView.setText("Монеты: " + coins);

        // Выбираем случайным образом, какое препятствие будет видимым в начале игры
        boolean isFirstObstacle = Math.random() < 0.5;
        if (isFirstObstacle) {
            obstacleImageView.setVisibility(View.VISIBLE);
            isObstacleVisible = true;
            obstacle2ImageView.setVisibility(View.INVISIBLE);
        } else {
            obstacleImageView.setVisibility(View.INVISIBLE);
            isObstacleVisible = false;
            obstacle2ImageView.setVisibility(View.VISIBLE);
            isObstacle2Visible = true;
            animateObstacle2();
        }
        animateCoin();


    }


    private void animateObstacle2() {
        obstacle2ImageView.setImageResource(R.drawable.hyena_animation);
        AnimationDrawable animation = (AnimationDrawable) obstacle2ImageView.getDrawable();
        animation.start();
    }

    private void animateCoin() {
        coinImageView.setImageResource(R.drawable.coin_animation);
        AnimationDrawable animation = (AnimationDrawable) coinImageView.getDrawable();
        animation.start();
    }

    private void updateGame() {

        float obstacleX = obstacleImageView.getX();
        float obstacle2X = obstacle2ImageView.getX();
        // Проверяем, есть ли уже видимое препятствие на экране
        boolean isObstacleVisible = obstacleX > -obstacleImageView.getWidth();
        boolean isObstacle2Visible = obstacle2X > -obstacle2ImageView.getWidth();
        obstacleX -= obstacleSpeed;
        obstacle2X -= obstacleSpeed;

        if (obstacleX < -obstacleImageView.getWidth()) {
            obstacleX = getWindowManager().getDefaultDisplay().getWidth();
            isObstacleVisible = true;
        }

        if (obstacle2X < -obstacle2ImageView.getWidth()) {
            obstacle2X = getWindowManager().getDefaultDisplay().getWidth();
            isObstacle2Visible = true;
        }

        obstacleImageView.setX(obstacleX);
        obstacle2ImageView.setX(obstacle2X);

        // Если нет видимых препятствий, выбираем случайным образом, какое препятствие появится
        if (!isObstacleVisible && !isObstacle2Visible) {
            boolean isFirstObstacle = Math.random() < 0.5;
            if (isFirstObstacle) {
                obstacleImageView.setVisibility(View.VISIBLE);
                isObstacleVisible = true;
            } else {
                obstacle2ImageView.setVisibility(View.VISIBLE);
                isObstacle2Visible = true;
            }
        }

        // Проверка столкновения персонажа с препятствием
        if ((isObstacleVisible && isCollision(characterImageView, obstacleImageView)) ||
                (isObstacle2Visible && isCollision(characterImageView, obstacle2ImageView))) {
            endGame();
        }
        boolean isJumpedOverObstacle = false;
        boolean isJumpedOverObstacle2 = false;

        // Проверка перепрыгивания препятствия
        if (isObstacleVisible && characterImageView.getX() > obstacleImageView.getX() + obstacleImageView.getWidth()) {
            if (!isJumpedOverObstacle) {
                // Успешное перепрыгивание препятствия
                // Увеличиваем счет на одно очко
                score++;
                scoreTextView.setText("Очки: " + score);
                isJumpedOverObstacle = true;
            }
        } else if (characterImageView.getX() < obstacleImageView.getX()) {
            isJumpedOverObstacle = false;
        }

        if (isObstacle2Visible && characterImageView.getX() > obstacle2ImageView.getX() + obstacle2ImageView.getWidth()) {
            if (!isJumpedOverObstacle2) {
                // Успешное перепрыгивание второго препятствия
                // Увеличиваем счет на одно очко
                score++;
                scoreTextView.setText("Очки: " + score);
                isJumpedOverObstacle2 = true;
            }
        } else if (characterImageView.getX() < obstacle2ImageView.getX()) {
            isJumpedOverObstacle2 = false;
        }

        // Обновление положения монетки
        float coinX = coinImageView.getX();
        coinX -= obstacleSpeed;
        if (coinX < -coinImageView.getWidth()) {
            // Монетка достигла края экрана, генерируем новое положение
            coinX = getWindowManager().getDefaultDisplay().getWidth();
            coinImageView.setVisibility(View.VISIBLE);
        }
        coinImageView.setX(coinX);

        // Увеличение скорости препятствия, птички и монетки по мере развития игры
        if (score % 50 == 0 && obstacleSpeed < 20) {
            obstacleSpeed++;
        }

        // Сброс флага coinCollected, если монетка находится за пределами экрана
        if (coinImageView.getX() + coinImageView.getWidth() < 0) {
            coinCollected = false;
        }

        if (coinImageView.getVisibility() == View.VISIBLE && isCollision(characterImageView, coinImageView)) {
            coinImageView.setVisibility(View.INVISIBLE);
            // Увеличиваем счет за подбор монетки
            coins++;
            mediaPlayer.start();
            coinsTextView.setText("Монеты: " + coins);
            int collectedCoins = 1; // Пример количества подсчитанных монет
            CoinDatabaseHelper coinDatabaseHelper = new CoinDatabaseHelper(this);
            coinDatabaseHelper.addCoin(collectedCoins);
        }


    }

    private AnimationDrawable loadJumpAnimationSkin1() {
        AnimationDrawable animation = new AnimationDrawable();
        animation.addFrame(getResources().getDrawable(R.drawable.jump_21), 400);
        animation.addFrame(getResources().getDrawable(R.drawable.jump_22), 400);
        animation.addFrame(getResources().getDrawable(R.drawable.jump_23), 400);
        animation.addFrame(getResources().getDrawable(R.drawable.jump_24), 400);
        animation.addFrame(getResources().getDrawable(R.drawable.jump_25), 400);
        animation.addFrame(getResources().getDrawable(R.drawable.jump_26), 400);
        animation.addFrame(getResources().getDrawable(R.drawable.jump_27), 400);
        return animation;
    }

    private AnimationDrawable loadJumpAnimationSkin2() {
        AnimationDrawable animation = new AnimationDrawable();
        animation.addFrame(getResources().getDrawable(R.drawable.jump_31), 400);
        animation.addFrame(getResources().getDrawable(R.drawable.jump_32), 400);
        animation.addFrame(getResources().getDrawable(R.drawable.jump_33), 400);
        animation.addFrame(getResources().getDrawable(R.drawable.jump_34), 400);
        animation.addFrame(getResources().getDrawable(R.drawable.jump_35), 400);
        animation.addFrame(getResources().getDrawable(R.drawable.jump_36), 400);

        // Добавьте другие кадры по необходимости

        return animation;
    }

    private AnimationDrawable loadJumpAnimationSkin3() {
        AnimationDrawable animation = new AnimationDrawable();
        animation.addFrame(getResources().getDrawable(R.drawable.jump_41), 400);
        animation.addFrame(getResources().getDrawable(R.drawable.jump_42), 400);
        animation.addFrame(getResources().getDrawable(R.drawable.jump_43), 400);
        animation.addFrame(getResources().getDrawable(R.drawable.jump_44), 400);
        animation.addFrame(getResources().getDrawable(R.drawable.jump_45), 400);
        animation.addFrame(getResources().getDrawable(R.drawable.jump_46), 400);
        return animation;
    }

    private AnimationDrawable loadJumpAnimationSkin4() {
        AnimationDrawable animation = new AnimationDrawable();
        animation.addFrame(getResources().getDrawable(R.drawable.jump_51), 400);
        animation.addFrame(getResources().getDrawable(R.drawable.jump_52), 400);
        animation.addFrame(getResources().getDrawable(R.drawable.jump_53), 400);
        animation.addFrame(getResources().getDrawable(R.drawable.jump_54), 400);
        animation.addFrame(getResources().getDrawable(R.drawable.jump_55), 400);
        animation.addFrame(getResources().getDrawable(R.drawable.jump_56), 400);
        animation.addFrame(getResources().getDrawable(R.drawable.jump_57), 400);
        return animation;
    }

    private AnimationDrawable loadDefaultJumpAnimation() {
        // Загрузка анимации прыжка по умолчанию из ресурсов
        AnimationDrawable animation = new AnimationDrawable();
        animation.addFrame(getResources().getDrawable(R.drawable.jump_1), 400);
        animation.addFrame(getResources().getDrawable(R.drawable.jump_2), 400);
        animation.addFrame(getResources().getDrawable(R.drawable.jump_3), 400);
        animation.addFrame(getResources().getDrawable(R.drawable.jump_4), 400);
        animation.addFrame(getResources().getDrawable(R.drawable.jump_5), 400);
        animation.addFrame(getResources().getDrawable(R.drawable.jump_6), 400);
        animation.addFrame(getResources().getDrawable(R.drawable.jump_7), 400);
        animation.addFrame(getResources().getDrawable(R.drawable.jump_8), 400);
        return animation;
    }

    private AnimationDrawable loadRunAnimationSkin1() {
        AnimationDrawable animation = new AnimationDrawable();
        animation.addFrame(getResources().getDrawable(R.drawable.run_21), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.run_22), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.run_23), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.run_24), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.run_26), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.run_27), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.run_28), 100);
        return animation;
    }

    private AnimationDrawable loadRunAnimationSkin2() {
        AnimationDrawable animation = new AnimationDrawable();
        animation.addFrame(getResources().getDrawable(R.drawable.run_31), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.run_32), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.run_33), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.run_34), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.run_35), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.run_36), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.run_37), 100);
        return animation;
    }

    private AnimationDrawable loadRunAnimationSkin3() {
        AnimationDrawable animation = new AnimationDrawable();
        animation.addFrame(getResources().getDrawable(R.drawable.run_41), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.run_42), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.run_43), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.run_44), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.run_45), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.run_46), 100);
        return animation;
    }

    private AnimationDrawable loadRunAnimationSkin4() {
        AnimationDrawable animation = new AnimationDrawable();
        animation.addFrame(getResources().getDrawable(R.drawable.run_51), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.run_52), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.run_53), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.run_54), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.run_55), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.run_56), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.run_57), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.run_58), 100);
        return animation;
    }

    private AnimationDrawable loadDefaultRunAnimation() {
        AnimationDrawable animation = new AnimationDrawable();
        animation.addFrame(getResources().getDrawable(R.drawable.run_1), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.run_2), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.run_3), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.run_4), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.run_5), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.run_6), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.run_7), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.run_8), 100);
        // Add other frames as needed
        return animation;
    }

    // Пример использования методов в методе jumpCharacter()
    private void jumpCharacter() {
        SharedPreferences sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        // Получение данных
        boolean isSkin1Purchased = sharedPreferences.getBoolean("skin1Purchased", false);
        boolean isSkin2Purchased = sharedPreferences.getBoolean("skin2Purchased", false);
        boolean isSkin3Purchased = sharedPreferences.getBoolean("skin3Purchased", false);
        boolean isSkin4Purchased = sharedPreferences.getBoolean("skin4Purchased", false);
        boolean isSkin5Purchased = sharedPreferences.getBoolean("skin5Purchased", false);
        int equippedSkinId = sharedPreferences.getInt("equippedSkinId", 0);

        if (!isJumping) {
            isJumping = true;

            AnimationDrawable jumpAnimation;
            AnimationDrawable runAnimation;

            if (equippedSkinId == SKIN_5_ID) {
                jumpAnimation = loadDefaultJumpAnimation();
                runAnimation = loadDefaultRunAnimation();
            } else if (equippedSkinId == SKIN_1_ID) {
                jumpAnimation = loadJumpAnimationSkin1();
                runAnimation = loadRunAnimationSkin1();
            } else if (equippedSkinId == SKIN_2_ID){
                jumpAnimation = loadJumpAnimationSkin2();
                runAnimation = loadRunAnimationSkin2();
            }else if (equippedSkinId == SKIN_3_ID){
                jumpAnimation = loadJumpAnimationSkin3();
                runAnimation = loadRunAnimationSkin3();
            }else{
                jumpAnimation = loadJumpAnimationSkin4();
                runAnimation = loadRunAnimationSkin4();
            }

            characterImageView.setImageDrawable(jumpAnimation);
            jumpAnimation.start();
            runAnimation.start();

            float characterY = characterImageView.getY();
            characterImageView.animate().yBy(-jumpHeight).setDuration(jumpDuration).withEndAction(new Runnable() {
                @Override
                public void run() {
                    characterImageView.animate().y(characterY).setDuration(jumpDuration).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            characterImageView.setImageDrawable(runAnimation);
                            runAnimation.start();
                            isJumping = false;
                        }
                    }).start();
                }
            }).start();
        }
    }




    private boolean isCollision(View view1, View view2) {
        characterRect = new Rect();
        view1.getHitRect(characterRect);
        obstacleRect = new Rect();
        view2.getHitRect(obstacleRect);
        return Rect.intersects(characterRect, obstacleRect);
    }


    private AnimationDrawable loadDeathAnimationSkin1() {
        AnimationDrawable animation = new AnimationDrawable();
        animation.addFrame(getResources().getDrawable(R.drawable.dead_21), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.dead_22), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.dead_23), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.dead_24), 100);
        // Добавьте другие кадры анимации для скина 1
        return animation;
    }

    private AnimationDrawable loadDeathAnimationSkin2() {
        AnimationDrawable animation = new AnimationDrawable();
        animation.addFrame(getResources().getDrawable(R.drawable.dead_31), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.dead_32), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.dead_33), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.dead_34), 100);
        // Добавьте другие кадры анимации для скина 2
        return animation;
    }

    private AnimationDrawable loadDeathAnimationSkin3() {
        AnimationDrawable animation = new AnimationDrawable();
        animation.addFrame(getResources().getDrawable(R.drawable.dead_41), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.dead_42), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.dead_43), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.dead_44), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.dead_45), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.dead_46), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.dead_47), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.dead_48), 100);
        return animation;
    }

    private AnimationDrawable loadDeathAnimationSkin4() {
        AnimationDrawable animation = new AnimationDrawable();
        animation.addFrame(getResources().getDrawable(R.drawable.dead_51), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.dead_52), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.dead_53), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.dead_54), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.dead_55), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.dead_56), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.dead_57), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.dead_58), 100);
        return animation;
    }

    private AnimationDrawable loadDefaultDeathAnimation() {
        AnimationDrawable animation = new AnimationDrawable();
        animation.addFrame(getResources().getDrawable(R.drawable.dead_1), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.dead_2), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.dead_3), 100);
        animation.addFrame(getResources().getDrawable(R.drawable.dead_4), 100);
        // Добавьте другие кадры анимации по умолчанию
        return animation;
    }



        private void endGame() {
            isGameRunning = false;
            handler.removeCallbacks(gameRunnable);

            // Передаем количество набранных монет в GameOverActivity
            Intent gameOverIntent = new Intent(GameActivity.this, GameOverActivity.class);
            gameOverIntent.putExtra("score", score);
            gameOverIntent.putExtra("coins", coins);
            startActivity(gameOverIntent);
            finish();

            // Проверяем, является ли текущий счет максимальным
            int maxScore = leaderboardDatabaseHelper.getMaxScoreFromDatabase();
            if (score > maxScore) {
                maxScore = score;
                leaderboardDatabaseHelper.saveMaxScoreToDatabase(maxScore);
            }
            SharedPreferences sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
            // Получение данных
            boolean isSkin1Purchased = sharedPreferences.getBoolean("skin1Purchased", false);
            boolean isSkin2Purchased = sharedPreferences.getBoolean("skin2Purchased", false);
            boolean isSkin3Purchased = sharedPreferences.getBoolean("skin3Purchased", false);
            boolean isSkin4Purchased = sharedPreferences.getBoolean("skin4Purchased", false);
            boolean isSkin5Purchased = sharedPreferences.getBoolean("skin5Purchased", false);
            int equippedSkinId = sharedPreferences.getInt("equippedSkinId", 0);

            // Создаем и запускаем анимацию смерти
            AnimationDrawable deathAnimation;
            if (equippedSkinId == SKIN_5_ID) {
                deathAnimation =  loadDefaultDeathAnimation();
            } else if(equippedSkinId == SKIN_1_ID){
                deathAnimation = loadDeathAnimationSkin1();

            }else if(equippedSkinId == SKIN_2_ID){
                deathAnimation = loadDeathAnimationSkin2();
            }else if(equippedSkinId == SKIN_3_ID){
                deathAnimation = loadDeathAnimationSkin3();
            }else{
                deathAnimation = loadDeathAnimationSkin4();
            }

            characterImageView.setImageDrawable(deathAnimation);
            deathAnimation.start();

        }
    /*private void animateCharacterDeath() {
        characterImageView.setImageResource(R.drawable.dead_animation);
        AnimationDrawable animation = (AnimationDrawable) characterImageView.getDrawable();
        animation.start();
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(gameRunnable);
        leaderboardDatabaseHelper.close();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}