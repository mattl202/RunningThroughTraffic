// CannonView.java
// Displays and controls the Cannon Game
package edu.augustana.csc490.gamestarter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainGameView extends SurfaceView implements SurfaceHolder.Callback
{
    private static final String TAG = "GameStarter"; // for Log.w(TAG, ...)

    private GameThread gameThread; // runs the main game loop
    private Activity mainActivity; // keep a reference to the main Activity

    private boolean isGameOver = true;

    private int x; // blue circle x
    private int y; // blue object y
    private int screenWidth;
    private int screenHeight;

    private Paint mainCharacterPaint;
    private Paint backgroundPaint;
    private Paint carPaint;
    private Paint streetPaint;
    private Paint centerLinePaint;
    //private Paint

    private int velocity;

    //THinking this is where I make my road start
    public MainGameView(Context context, AttributeSet atts)
    {
        super(context, atts);
        mainActivity = (Activity) context;

        getHolder().addCallback(this);

        mainCharacterPaint = new Paint();
        mainCharacterPaint.setColor(Color.RED);
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.GREEN);
        carPaint = new Paint();
        carPaint.setColor(Color.BLACK);
        streetPaint = new Paint(Color.GRAY);
        centerLinePaint = new Paint();
        centerLinePaint.setColor(Color.YELLOW);
    }

    // called when the size changes (and first time, when view is created)
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);

        screenWidth = w;
        screenHeight = h;

        startNewGame();
    }

    public void startNewGame()
    {
        this.x = screenWidth/2;
        this.y = (int) (screenHeight*0.85);

        if (isGameOver)
        {
            isGameOver = false;
            gameThread = new GameThread(getHolder());
            gameThread.start(); // start the main game loop going
        }
    }


    private void gameStep()
    {
        y-=velocity;
    }

    public void updateView(Canvas canvas)
    {
        if (canvas != null) {
           // canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);
            drawBackground(canvas);
            canvas.drawCircle(x, y, 20, mainCharacterPaint);
        }
    }

    private void drawBackground(Canvas canvas) {
        canvas.drawRect(0,0,canvas.getWidth(),canvas.getHeight(),backgroundPaint);
        //drawRect goes (left top right bottom)
        canvas.drawRect(0,(int)(.30*canvas.getHeight()),canvas.getWidth(),(int)(.70*canvas.getHeight()),streetPaint);
        drawCenterLine(canvas);
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
            this.velocity += 4;
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

                    // lock the surfaceHolder for drawing
                    synchronized(surfaceHolder)
                    {
                        gameStep();         // update game state
                        updateView(canvas); // draw using the canvas
                    }
                    Thread.sleep(10); // if you want to slow down the action...
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