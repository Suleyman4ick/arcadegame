package com.example.arcadegame;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.arcadegame.database.CoinDatabaseHelper;

import java.util.Locale;

public class SkinShopActivity extends AppCompatActivity {
    private TextView coinsTextView;
    private TextView skin1TextView;
    private TextView skin2TextView;
    private TextView skin3TextView;
    private TextView skin4TextView;
    private ImageButton buySkin1Button;
    private ImageButton buySkin2Button;
    private ImageButton buySkin3Button;
    private ImageButton buySkin4Button;
    private int totalCoinCount;
    private CoinDatabaseHelper coinDatabaseHelper;
    private boolean isSkin1Purchased;
    private boolean isSkin2Purchased;
    private boolean isSkin3Purchased;
    private boolean isSkin4Purchased;
    private boolean isSkin5Purchased;
    private Button wearButton1;
    private Button wearButton2;
    private Button wearButton3;
    private Button wearButton4;
    private Button wearButton5;
    private static final int SKIN_1_ID = 1; // Идентификатор скина 1
    private static final int SKIN_2_ID = 2; // Идентификатор скина 2
    private static final int SKIN_3_ID = 3; // Идентификатор скина 1
    private static final int SKIN_4_ID = 4; // Идентификатор скина 2
    private static final int SKIN_5_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_skin_shop);

        coinsTextView = findViewById(R.id.coinsTextView);
        skin1TextView = findViewById(R.id.skin1PriceTextView);
        skin2TextView = findViewById(R.id.skin2PriceTextView);
        skin3TextView = findViewById(R.id.skin3PriceTextView);
        skin4TextView = findViewById(R.id.skin4PriceTextView);

        buySkin1Button = findViewById(R.id.skin1Button);
        buySkin2Button = findViewById(R.id.skin2Button);
        buySkin3Button = findViewById(R.id.skin3Button);
        buySkin4Button = findViewById(R.id.skin4Button);

        ImageButton backButton = findViewById(R.id.back_button);

        wearButton1 = findViewById(R.id.button);
        wearButton2 = findViewById(R.id.button2);
        wearButton3 = findViewById(R.id.button3);
        wearButton4 = findViewById(R.id.button4);
        wearButton5 = findViewById(R.id.button5);


        coinDatabaseHelper = new CoinDatabaseHelper(this);
        totalCoinCount = coinDatabaseHelper.getTotalCoinCount();
        coinsTextView.setText(String.format(Locale.getDefault(), "Монеты: %d", totalCoinCount));

        // Проверяем, куплены ли скины и обновляем текстовые вью и доступность кнопок соответственно
        SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        isSkin1Purchased = preferences.getBoolean("skin1Purchased", false);
        isSkin2Purchased = preferences.getBoolean("skin2Purchased", false);
        isSkin3Purchased = preferences.getBoolean("skin3Purchased", false);
        isSkin4Purchased = preferences.getBoolean("skin4Purchased", false);
        updateSkinPurchasedStatus();

        buySkin1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int skin1Price = 10; // Задайте стоимость скина 1 в монетах

                if (isSkin1Purchased) {
                    // Если скин уже куплен
                    Toast.makeText(SkinShopActivity.this, "Скин 1 уже куплен", Toast.LENGTH_SHORT).show();
                } else if (totalCoinCount >= skin1Price) {
                    // Покупка скина 1
                    // Обновление количества монет
                    int newCoinCount = totalCoinCount - skin1Price;
                    coinDatabaseHelper.updateCoinCount(newCoinCount);
                    totalCoinCount = newCoinCount;
                    coinsTextView.setText(String.format(Locale.getDefault(), "Монеты: %d", newCoinCount));

                    // Сохранение состояния покупки скина и его идентификатора
                    SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("skin1Purchased", true);
                    editor.putInt("equippedSkinId", SKIN_1_ID);
                    editor.apply();

                    // Обновление текстового вида skin1PriceTextView
                    skin1TextView.setText("Куплено");
                    isSkin1Purchased = true;
                    updateSkinPurchasedStatus();

                    Toast.makeText(SkinShopActivity.this, "Скин 1 куплен", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SkinShopActivity.this, "Недостаточно монет для покупки", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buySkin2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int skin2Price = 15; // Задайте стоимость скина 2 в монетах

                if (isSkin2Purchased) {
                    // Если скин уже куплен
                    Toast.makeText(SkinShopActivity.this, "Скин 2 уже куплен", Toast.LENGTH_SHORT).show();
                } else if (totalCoinCount >= skin2Price) {
                    // Покупка скина 2
                    // Обновление количества монет
                    int newCoinCount = totalCoinCount - skin2Price;
                    coinDatabaseHelper.updateCoinCount(newCoinCount);
                    totalCoinCount = newCoinCount;
                    coinsTextView.setText(String.format(Locale.getDefault(), "Монеты: %d", newCoinCount));

                    // Сохранение состояния покупки скина и его идентификатора
                    SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("skin2Purchased", true);
                    editor.putInt("equippedSkinId", SKIN_2_ID);
                    editor.apply();

                    // Обновление текстового вида skin2PriceTextView
                    skin2TextView.setText("Куплено");
                    isSkin2Purchased = true;
                    updateSkinPurchasedStatus();

                    Toast.makeText(SkinShopActivity.this, "Скин 2 куплен", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SkinShopActivity.this, "Недостаточно монет для покупки", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buySkin3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int skin3Price = 20; // Задайте стоимость скина 3 в монетах

                if (isSkin3Purchased) {
                    // Если скин уже куплен
                    Toast.makeText(SkinShopActivity.this, "Скин 3 уже куплен", Toast.LENGTH_SHORT).show();
                } else if (totalCoinCount >= skin3Price) {
                    int newCoinCount = totalCoinCount - skin3Price;
                    coinDatabaseHelper.updateCoinCount(newCoinCount);
                    totalCoinCount = newCoinCount;
                    coinsTextView.setText(String.format(Locale.getDefault(), "Монеты: %d", newCoinCount));

                    // Сохранение состояния покупки скина и его идентификатора
                    SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("skin3Purchased", true);
                    editor.putInt("equippedSkinId", SKIN_3_ID);
                    editor.apply();

                    // Обновление текстового вида skin2PriceTextView
                    skin3TextView.setText("Куплено");
                    isSkin3Purchased = true;
                    updateSkinPurchasedStatus();

                    Toast.makeText(SkinShopActivity.this, "Скин 3 куплен", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SkinShopActivity.this, "Недостаточно монет для покупки", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buySkin4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int skin4Price = 25; // Задайте стоимость скина 4 в монетах

                if (isSkin4Purchased) {
                    // Если скин уже куплен
                    Toast.makeText(SkinShopActivity.this, "Скин 4 уже куплен", Toast.LENGTH_SHORT).show();
                } else if (totalCoinCount >= skin4Price) {
                    int newCoinCount = totalCoinCount - skin4Price;
                    coinDatabaseHelper.updateCoinCount(newCoinCount);
                    totalCoinCount = newCoinCount;
                    coinsTextView.setText(String.format(Locale.getDefault(), "Монеты: %d", newCoinCount));

                    // Сохранение состояния покупки скина и его идентификатора
                    SharedPreferences preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("skin4Purchased", true);
                    editor.putInt("equippedSkinId", SKIN_4_ID);
                    editor.apply();

                    // Обновление текстового вида skin2PriceTextView
                    skin4TextView.setText("Куплено");
                    isSkin4Purchased = true;
                    updateSkinPurchasedStatus();

                    Toast.makeText(SkinShopActivity.this, "Скин 4 куплен", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SkinShopActivity.this, "Недостаточно монет для покупки", Toast.LENGTH_SHORT).show();
                }
            }
        });



        wearButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSkin1Purchased) {
                    SharedPreferences sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("skin1Purchased", true);
                    editor.putInt("equippedSkinId", SKIN_1_ID);
                    editor.apply();

                    Toast.makeText(SkinShopActivity.this, "Скин 1 надет", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(SkinShopActivity.this, "Скин 1 не куплен", Toast.LENGTH_SHORT).show();
                }
            }
        });


        wearButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSkin2Purchased) {
                    SharedPreferences sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("skin2Purchased", true);
                    editor.putInt("equippedSkinId", SKIN_2_ID);
                    editor.apply();

                    Toast.makeText(SkinShopActivity.this, "Скин 2 надет", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SkinShopActivity.this, "Скин 2 не куплен", Toast.LENGTH_SHORT).show();
                }
            }
        });

        wearButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSkin3Purchased) {
                    SharedPreferences sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("skin3Purchased", true);
                    editor.putInt("equippedSkinId", SKIN_3_ID);
                    editor.apply();
                    Toast.makeText(SkinShopActivity.this, "Скин 3 надет", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SkinShopActivity.this, "Скин 3 не куплен", Toast.LENGTH_SHORT).show();
                }
            }
        });

        wearButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSkin4Purchased) {
                    SharedPreferences sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("skin4Purchased", true);
                    editor.putInt("equippedSkinId", SKIN_4_ID);
                    editor.apply();
                    Toast.makeText(SkinShopActivity.this, "Скин 4 надет", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SkinShopActivity.this, "Скин 4 не куплен", Toast.LENGTH_SHORT).show();
                }
            }
        });

        wearButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    SharedPreferences sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("skin5Purchased", true);
                    editor.putInt("equippedSkinId", SKIN_5_ID);
                    editor.apply();
                    Toast.makeText(SkinShopActivity.this, "Скин 0 надет", Toast.LENGTH_SHORT).show();

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updateSkinPurchasedStatus() {
        if (isSkin1Purchased) {
            skin1TextView.setText("Куплено");
            buySkin1Button.setEnabled(false);
            wearButton1.setEnabled(true);
        } else {
            skin1TextView.setText("10 монет");
            buySkin1Button.setEnabled(true);
            wearButton1.setEnabled(false);
        }

        if (isSkin2Purchased) {
            skin2TextView.setText("Куплено");
            buySkin2Button.setEnabled(false);
            wearButton2.setEnabled(true);
        } else {
            skin2TextView.setText("15 монет");
            buySkin2Button.setEnabled(true);
            wearButton2.setEnabled(false);
        }
        if (isSkin3Purchased) {
            // Скин 3 уже куплен
            skin3TextView.setText("Куплено");
            buySkin3Button.setEnabled(false);
            wearButton3.setEnabled(true);
        } else {
            // Скин 3 не куплен
            skin3TextView.setText("20 монет");
            buySkin3Button.setEnabled(true);
            wearButton3.setEnabled(false);
        }
        if (isSkin4Purchased) {
            // Скин 4 уже куплен
            skin4TextView.setText("Куплено");
            buySkin4Button.setEnabled(false);
            wearButton4.setEnabled(true);
        } else {
            // Скин 4 не куплен
            skin4TextView.setText("25 монет");
            buySkin4Button.setEnabled(true);
            wearButton4.setEnabled(false);
        }
    }


    private void resetSkinStatus() {
        isSkin1Purchased = false;
        isSkin2Purchased = false;
        isSkin3Purchased = false;
        isSkin4Purchased = false;
        updateSkinPurchasedStatus();
    }
}