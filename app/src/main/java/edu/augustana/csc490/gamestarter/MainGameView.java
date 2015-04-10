// CannonView.java
// Displays and controls the Cannon Game
package edu.augustana.csc490.gamestarter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainGameView extends SurfaceView implements SurfaceHolder.Callback
{
    private static final String TAG = "GameStarter"; // for Log.w(TAG, ...)

    private GameThread gameThread; // runs the main game loop
    private Activity mainActivity; // keep a reference to the main Activity

    private boolean isGameOver = true;

    private int x; // blue box x
    private int y; // blue box y
    private int screenWidth;
    private int screenHeight;

    private Paint mainCharacterPaint;
    private Paint backgroundPaint;
    private Paint carPaint;
    private Paint streetPaint;
    private Paint centerLinePaint;
    // private Paint

    private int yourVelocity;
    private int carVelocity = 25;

    Car car = new Car();
    Car car1 = new Car(25);

    public int americanRightTop;

    public ArrayList<Car> cars;

    public int level = 1;

    // Thinking this is where I make my road start
    public MainGameView(Context context, AttributeSet atts)
    {
        super(context, atts);
        mainActivity = (Activity) context;

        getHolder().addCallback(this);

        // I choose each color here
        mainCharacterPaint = new Paint();
        mainCharacterPaint.setColor(Color.RED);
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.GREEN);
        carPaint = new Paint();
        carPaint.setColor(Color.BLACK);
        streetPaint = new Paint();
        streetPaint.setColor(Color.GRAY);
        centerLinePaint = new Paint();
        centerLinePaint.setColor(Color.YELLOW);


        cars = new ArrayList<Car>();
        cars.add(car);
        cars.add(car1);

    }

    // called when the size changes (and first time, when view is created)
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);

        screenWidth = w;
        screenHeight = h;

        try {
            startNewGame(level);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void startNewGame(int level) throws InterruptedException {



        cars.clear();




        for(int i = 0; i < level; i++) {
            Car c = new Car(carVelocity);
            if (level < 4){
                //Car c = new Car(carVelocity);
                c.x = c.x - 2 * (i*screenWidth/level);
                cars.add(c);
            } else if (level == 4) {
                c.x = c.x - (i*screenWidth/level);
                cars.add(c);
            } else {
                c.x = c.x - 2 * (i*screenWidth/level);
                cars.add(c);

                Car c2 = new Car(carVelocity, "left");
                //c2.x = screenWidth;
                //c2.velocity = -c2.velocity;
                cars.add(c2);
            }

        }
        //cars.add(new Car(40));
        this.x = screenWidth/2;
        this.y = (int) (screenHeight*0.85);

        if (isGameOver)
        {
            isGameOver = false;
            gameThread = new GameThread(getHolder());
            gameThread.start(); // start the main game loop going
        }
    }


    private void gameStep() throws InterruptedException {

        y = y - yourVelocity;

        for (Car c : cars) {
            c.move(screenWidth);

        }
        //car.move();




        // Shoot, not sure what to do...
        //TODO THIS!
        if(y < screenHeight*.10){
            Thread.sleep(400);
            level++;
            yourVelocity = 0;
            startNewGame(level);
        }


    }

    // This is when the character runs
    public void updateView(Canvas canvas)
    {
        if (canvas != null) {
           // canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);
            drawBackground(canvas);

            // Draw the main character!
            // Well, I need to work on this next!
            canvas.drawCircle(x, y, 20, mainCharacterPaint);
            //Rect rect = new Rect(10,10,10,10);

            for (Car c : cars) {
                //c.move();
                canvas.drawRect(c.getRect(canvas), carPaint);
            }


            //canvas.drawRect(20,canvas.getHeight()/2,60,canvas.getHeight()/2+40,carPaint);
        }
    }

    private void drawBackground(Canvas canvas) {
        canvas.drawRect(0,0,canvas.getWidth(),canvas.getHeight(),backgroundPaint);
        //drawRect goes (left top right bottom)
        canvas.drawRect(0,(int)(.30*canvas.getHeight()),canvas.getWidth(),(int)(.70*canvas.getHeight()),streetPaint);
        drawCenterLine(canvas);
        Paint c = new Paint();
        c.setColor(Color.WHITE);
        canvas.drawText("" + level,100,(int)(canvas.getHeight()*.85),c);
        //canvas.scale();
        //TextView v = (TextView)findViewById(R.id.textView2);
        //v.setText("" + level);

    }

    private void drawCenterLine(Canvas canvas) {
        for(int i = 0; i<4; i++) {
            canvas.drawRect(i * 200,(((int)canvas.getHeight()/2)+10), (int)(canvas.getWidth()/6) + i * 200,((int)canvas.getHeight()/2)-10,centerLinePaint);
        }
    }

    // stop the game; may be called by the MainGameFragment onPause
    public void stopGame()
    {
        if (gameThread != null)
            gameThread.setRunning(false);
    }

    // release resources; may be called by MainGameFragment onDestroy
    public void releaseResources()
    {
        // release any resources (e.g. SoundPool stuff)
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    // called when the surface is destroyed
    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        // ensure that thread terminates properly
        boolean retry = true;
        gameThread.setRunning(false); // terminate gameThread

        while (retry)
        {
            try
            {
                gameThread.join(); // wait for gameThread to finish
                retry = false;
            }
            catch (InterruptedException e)
            {
                Log.e(TAG, "Thread interrupted", e);
            }
        }
    }

        @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        if (e.getAction() == MotionEvent.ACTION_DOWN)
        {
            //this.x = (int) e.getX();
            //this.y = (int) e.getY();
            this.yourVelocity += 10;
        }

        return true;
    }

    // Thread subclass to run the main game loop
    private class GameThread extends Thread
    {
        private SurfaceHolder surfaceHolder; // for manipulating canvas
        private boolean threadIsRunning = true; // running by default

        // initializes the surface holder
        public GameThread(SurfaceHolder holder)
        {
            surfaceHolder = holder;
            setName("GameThread");
        }

        // changes running state
        public void setRunning(boolean running)
        {
            threadIsRunning = running;
        }



        @Override
        public void run()
        {

            Canvas canvas = null;

            while (threadIsRunning)
            {
                try
                {
                    // get Canvas for exclusive drawing from this thread
                    canvas = surfaceHolder.lockCanvas(null);

                    final Timer timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        int i = Integer.parseInt("100");
                        public void run() {
                            System.out.println(i--);
                            if (i< 0)
                                timer.cancel();
                        }
                    }, 0, 1000);

                    /* new CountDownTimer(30000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                        }

                        public void onFinish() {
                            //mTextField.setText("done!");
                        }
                    }.start(); */

                    // lock the surfaceHolder for drawing
                    synchronized(surfaceHolder)
                    {
                        gameStep();         // update game state
                        updateView(canvas); // draw using the canvas
                    }
                    Thread.sleep(20); // if you want to slow down the action...
                } catch (InterruptedException ex) {
                    Log.e(TAG,ex.toString());
                }
                finally  // regardless if any errors happen...
                {
                    // make sure we unlock canvas so other threads can use it
                    if (canvas != null)
                        surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}