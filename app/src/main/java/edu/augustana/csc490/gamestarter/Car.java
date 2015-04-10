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
    int top,right,bottom,left;

    public Car() {
        velocity = 5;
        height = 40;
        width = 150;
        x = 0;
        y = 0;

    }

    public Car(int velocity) {
        this.velocity = velocity;
        new Car();
        height = 40;
        width = 150;
        x = 0;
        this.direction = "right";
        //y = 200;
    }

    public Car(int velocity, String direction) {
        new Car(velocity);
        if(direction.equals("left")) {
            //this.velocity = -velocity;
            this.velocity = -25;
            this.direction = direction;
        } else {
            this.velocity = velocity;
        }
        this.direction = direction;
        height = 40;
        width = 150;
        //y = 200;

        //x = ;
    }

    public void move(int screenWidth) {
            x = x + velocity;
        if (direction.equals("right")) {
            x = x%(screenWidth*2);
            if (right == 0) x = x + ((int)Math.ceil(Math.random()*10)-5);
            //x = x + ((int)Math.ceil(Math.random()*10)-5);
        } else {
            if(x < 0 - width) {
                //x = (screenWidth*2);
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
        left = x - width;
        bottom = canvas.getHeight() * 2 / 3 + y;
        right = x;
        top = canvas.getHeight() / 2 + 40 + height + y;
        return new Rect(left, top, right, bottom);
    }
    public Rect getLeftRect(Canvas canvas) {
        left = canvas.getWidth();
        right = canvas.getWidth() + width;
        top = canvas.getHeight() * 1 / 3;
        bottom = canvas.getHeight() / 2 - 40 + height;
        return new Rect(left, top, right, bottom);
    }
}
