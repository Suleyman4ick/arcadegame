package com.example.arcadegame;

import java.io.Serializable;

public class Skin implements Serializable {
    private String name;
    private int imageResId;

    public Skin(String name, int imageResId) {
        this.name = name;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }
}
