package com.ace.easyteacher.Utils;

/**
 * Created by lenovo on 2016-3-24.
 */
public class ClassScoreUtils {
    String name;
    int color;
    float score;


    public ClassScoreUtils(String name, int color, float score) {
        this.name = name;
        this.color = color;
        this.score = score;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
