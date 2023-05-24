package com.example.pingpong;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

public class MainActivity extends Activity implements SurfaceHolder.Callback {

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Paint mPaint;
    private float mPlayerPaddleX, mPlayerPaddleY, mPlayerPaddleWidth, mPlayerPaddleHeight;
    private float mEnemyPaddleX, mEnemyPaddleY, mEnemyPaddleWidth, mEnemyPaddleHeight;

    private float mBallX, mBallY, mBallRadius, mBallSpeedX, mBallSpeedY;
    private float mScreenWidth, mScreenHeight;
    private static final int PADDLE_COLOR = Color.WHITE;
    private static final int BALL_COLOR = Color.BLUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSurfaceView = new SurfaceView(this);
        setContentView(mSurfaceView);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        mSurfaceView.setFocusable(true);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int orientation = newConfig.orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Landscape mode
            mPlayerPaddleWidth = mScreenWidth * 0.1f;
            mPlayerPaddleHeight = mScreenHeight * 0.05f;
            mPlayerPaddleX = (mScreenWidth - mPlayerPaddleWidth) / 2f;
            mPlayerPaddleY = mScreenHeight * 0.9f;

            mEnemyPaddleWidth = mScreenWidth * 0.1f;
            mEnemyPaddleHeight = mScreenHeight * 0.05f;
            mEnemyPaddleX = (mScreenWidth - mEnemyPaddleWidth) / 2f;
            mEnemyPaddleY = mScreenHeight * 0.1f;
        } else {
            // Portrait mode
            mPlayerPaddleWidth = mScreenWidth * 0.2f;
            mPlayerPaddleHeight = mScreenHeight * 0.05f;
            mPlayerPaddleX = (mScreenWidth - mPlayerPaddleWidth) / 2f;
            mPlayerPaddleY = mScreenHeight * 0.9f;

            mEnemyPaddleWidth = mScreenWidth * 0.5f;
            mEnemyPaddleHeight = mScreenHeight * 0.05f;
            mEnemyPaddleX = (mScreenWidth - mEnemyPaddleWidth) / 2f;
            mEnemyPaddleY = mScreenHeight * 0.1f;
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mScreenWidth = mSurfaceView.getWidth();
        mScreenHeight = mSurfaceView.getHeight();


        mBallRadius = mScreenWidth * 0.03f;
        mBallX = mScreenWidth / 2f;
        mBallY = mScreenHeight / 2f;
        mBallSpeedX = mScreenWidth * 0.005f;
        mBallSpeedY = mScreenHeight * 0.005f;

        mPlayerPaddleWidth = mScreenWidth * 0.2f;
        mPlayerPaddleHeight = mScreenHeight * 0.05f;
        mPlayerPaddleX = (mScreenWidth - mPlayerPaddleWidth) / 2f;
        mPlayerPaddleY = mScreenHeight * 0.9f;

        mEnemyPaddleWidth = mScreenWidth * 0.2f;
        mEnemyPaddleHeight = mScreenHeight * 0.05f;
        mEnemyPaddleX = (mScreenWidth - mEnemyPaddleWidth) / 2f;
        mEnemyPaddleY = mScreenHeight * 0.1f;


        startGameLoop();
    }



    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Do nothing
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // Do nothing
    }

    private void startGameLoop() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    update();
                    draw();
                    try {
                        Thread.sleep(16);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    private void draw() {
        Canvas canvas = mSurfaceHolder.lockCanvas();

        if (canvas != null) {
            Paint linePaint = new Paint();
            linePaint.setColor(Color.WHITE);
            linePaint.setStrokeWidth(10);
            canvas.drawLine(0, mScreenHeight/2, mScreenWidth, mScreenHeight/2, linePaint);
            canvas.drawColor(Color.BLACK);


            mPaint.setColor(PADDLE_COLOR);
            canvas.drawRect(mPlayerPaddleX, mPlayerPaddleY, mPlayerPaddleX + mPlayerPaddleWidth, mPlayerPaddleY + mPlayerPaddleHeight, mPaint);
            canvas.drawRect(mEnemyPaddleX, mEnemyPaddleY, mEnemyPaddleX + mEnemyPaddleWidth, mEnemyPaddleY + mEnemyPaddleHeight, mPaint);

            mPaint.setColor(BALL_COLOR);
            canvas.drawCircle(mBallX, mBallY, mBallRadius, mPaint);

            mSurfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void update() {
        mBallX += mBallSpeedX;
        mBallY += mBallSpeedY;

        if (mBallX - mBallRadius < 0) {
            mBallX = mBallRadius;
            mBallSpeedX = -mBallSpeedX;
        } else if (mBallX + mBallRadius > mScreenWidth) {
            mBallX = mScreenWidth - mBallRadius;
            mBallSpeedX = -mBallSpeedX;
        }

        if (mBallY - mBallRadius < 0) {
            mBallY = mBallRadius;
            mBallSpeedY = -mBallSpeedY;
        } else if (mBallY + mBallRadius > mScreenHeight) {
            mBallY = mScreenHeight - mBallRadius;
            mBallSpeedY = -mBallSpeedY;
        }

        if (mBallY + mBallRadius >= mPlayerPaddleY) {
            float paddleLeft = mPlayerPaddleX;
            float paddleRight = mPlayerPaddleX + mPlayerPaddleWidth;
            float paddleTop = mPlayerPaddleY;
            float paddleBottom = mPlayerPaddleY + mPlayerPaddleHeight;

            if (mBallX + mBallRadius >= paddleLeft && mBallX - mBallRadius <= paddleRight) {
                mBallY = paddleTop - mBallRadius;
                mBallSpeedY = -mBallSpeedY;
            } else if (mBallY + mBallRadius >= paddleTop && mBallY - mBallRadius <= paddleBottom) {
                if (mBallX + mBallRadius >= paddleLeft && mBallX < mPlayerPaddleX) {
                    mBallX = paddleLeft - mBallRadius;
                    mBallSpeedX = -mBallSpeedX;
                } else if (mBallX - mBallRadius <= paddleRight && mBallX > mPlayerPaddleX + mPlayerPaddleWidth) {
                    mBallX = paddleRight + mBallRadius;
                    mBallSpeedX = -mBallSpeedX;
                }
            }
        }

        if (mBallY - mBallRadius <= mEnemyPaddleY + mEnemyPaddleHeight) {
            float paddleLeft = mEnemyPaddleX;
            float paddleRight = mEnemyPaddleX + mEnemyPaddleWidth;
            float paddleTop = mEnemyPaddleY;
            float paddleBottom = mEnemyPaddleY + mEnemyPaddleHeight;

            if (mBallX + mBallRadius >= paddleLeft && mBallX - mBallRadius <= paddleRight) {
                mBallY = paddleBottom + mBallRadius;
                mBallSpeedY = -mBallSpeedY;
            } else if (mBallY - mBallRadius <= paddleBottom && mBallY + mBallRadius >= paddleTop) {
                if (mBallX + mBallRadius >= paddleLeft && mBallX < mEnemyPaddleX) {
                    mBallX = paddleLeft - mBallRadius;
                    mBallSpeedX = -mBallSpeedX;
                } else if (mBallX - mBallRadius <= paddleRight && mBallX > mEnemyPaddleX + mEnemyPaddleWidth) {
                    mBallX = paddleRight + mBallRadius;
                    mBallSpeedX = -mBallSpeedX;
                }
            }
        }
        float enemyPaddleCenterX = mEnemyPaddleX + mEnemyPaddleWidth / 2f;
        float ballCenterX = mBallX;

        if (ballCenterX > enemyPaddleCenterX) {
            mEnemyPaddleX += 5;
        } else if (ballCenterX < enemyPaddleCenterX) {
            mEnemyPaddleX -= 5;
        }

        if (mEnemyPaddleX < 0) {
            mEnemyPaddleX = 0;
        } else if (mEnemyPaddleX + mEnemyPaddleWidth > mScreenWidth) {
            mEnemyPaddleX = mScreenWidth - mEnemyPaddleWidth;
        }
    }

    private void resetBall(){
        mBallX = mScreenWidth / 2f;
        mBallY = mScreenHeight / 2f;
        mBallSpeedX = mBallSpeedX * (new Random().nextBoolean() ? 1 : -1 );
        mBallSpeedY = mBallSpeedY * (new Random().nextBoolean() ? 1 : -1 );
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                mPlayerPaddleX = event.getX() - mPlayerPaddleWidth / 2f;
                break;
        }
        return true;

    }
}