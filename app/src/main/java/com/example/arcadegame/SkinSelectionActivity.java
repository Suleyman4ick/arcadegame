package com.example.arcadegame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arcadegame.database.SkinAdapter;

import java.util.ArrayList;
import java.util.List;

public class SkinSelectionActivity extends AppCompatActivity implements SkinAdapter.OnSkinClickListener {
    private RecyclerView skinRecyclerView;
    private Button selectSkinButton;
    private SkinAdapter skinAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_skin_selection);

        skinRecyclerView = findViewById(R.id.skinRecyclerView);
        selectSkinButton = findViewById(R.id.selectSkinButton);


        // Создаем список скинов
        List<Skin> skinList = createSkinList();

        // Создаем адаптер и устанавливаем его для RecyclerView
        skinAdapter = new SkinAdapter(skinList, this);
        skinRecyclerView.setAdapter(skinAdapter);
        skinRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView skinRecyclerView = findViewById(R.id.skinRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        skinRecyclerView.setLayoutManager(layoutManager);


        selectSkinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получаем выбранный скин из адаптера
                Skin selectedSkin = skinAdapter.getSelectedSkin();

                if (selectedSkin != null) {
                    // Передаем выбранный скин в GameActivity
                    Intent intent = new Intent(SkinSelectionActivity.this, GameActivity.class);
                    intent.putExtra("selectedSkin", selectedSkin);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }


    private List<Skin> createSkinList() {
        List<Skin> skinList = new ArrayList<>();

        // Добавьте свою логику для создания списка скинов
        // Например:
        skinList.add(new Skin("Скин 1", R.drawable.hero_12));
        skinList.add(new Skin("Скин 2", R.drawable.hero_31));
        skinList.add(new Skin("Скин 3", R.drawable.hero_21));

        return skinList;
    }

    @Override
    public void onSkinClick(Skin skin) {
        // Обработчик нажатия на элемент списка скинов
        Intent intent = new Intent(SkinSelectionActivity.this, GameActivity.class);
        intent.putExtra("selectedSkin", skin); // Передача объекта Skin в другую активность
        startActivity(intent);
    }
}
