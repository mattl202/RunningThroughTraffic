package edu.augustana.csc490.gamestarter;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by Matthew Leja on 3/30/2015.
 */
class Car {
    int velocity;
    int x;
    int y;
    int height;
    int width;

    public Car() {
        velocity = 5;
        height = 40;
        width = 150;
        x = 0;

    }

    public Car(int velocity) {
        this.velocity = velocity;
        new Car();
        height = 40;
        width = 150;
        x = 0;
    }

    public void move() {
        x += velocity;

    }

    public Rect getRightRect(Canvas canvas) {
        int left = x - width;
        int top = canvas.getHeight() * 2 / 3;
        int right = x;
        int bottom = canvas.getHeight() / 2 + 40 + height;
        return new Rect(left, top, right, bottom);
    }
}
