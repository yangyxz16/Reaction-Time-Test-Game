package com.example.xxy180008asg5;

import android.graphics.Color;

/**
 * GamesScore class for shape object
 * which contains shape type, color, total life time, remain life time, x and y coordinates and length
 * Written by Xizhen Yang for CS6326.001, Assignment 5, starting March 28, 2020
 * NetID: XXY180008
 */
public class Shape {
    private int type; // 0 for circle, 1 for square
    private int color;
    private int life;
    private int lifeTime;

    private float x;
    private float y;
    private float length;

    public Shape(int type, int color, int life, int lifeTime, float x, float y, float length) {
        this.type = type;
        this.color = color;
        this.life = life;
        this.lifeTime = lifeTime;
        this.x = x;
        this.y = y;
        this.length = length;
    }

    public int getType() {
        return type;
    }

    public int getColor() {
        return color;
    }

    public int gerLife() {return life;}

    public int getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(int lifeTime) {
        this.lifeTime = lifeTime;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getLength() {
        return length;
    }
}
