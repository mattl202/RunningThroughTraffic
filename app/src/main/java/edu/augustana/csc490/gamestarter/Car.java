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
    String direction = "right";

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
        this.direction = "right";
    }

    public Car(int velocity, String direction) {
        if(direction.equals("left")) {
            this.velocity = -velocity;
            this.direction = direction;
        }
    }

    public void move(int screenWidth) {
            x += velocity;
        if (direction.equals("right")) {
            x = x%(screenWidth*2);
        } else {
            if(x < 0 - width) {
                x = (screenWidth*2);
            }
        }

    }

    public Rect getRect(Canvas canvas) {
        if (direction.equals("left")) {
            return getLeftRect(canvas);
        } else {
            return  getRightRect(canvas);
        }
    }

    public Rect getRightRect(Canvas canvas) {
        int left = x - width;
        int top = canvas.getHeight() * 2 / 3;
        int right = x;
        int bottom = canvas.getHeight() / 2 + 40 + height;
        return new Rect(left, top, right, bottom);
    }
    public Rect getLeftRect(Canvas canvas) {
        int left = canvas.getWidth();
        int right = canvas.getWidth() + width;
        int top = canvas.getHeight() * 1 / 3;
        int bottom = canvas.getHeight() / 2 - 40 + height;
        return new Rect(left, top, right, bottom);
    }
}
