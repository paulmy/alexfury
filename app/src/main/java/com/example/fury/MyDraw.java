package com.example.fury;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


public class MyDraw extends View {
    private Sprite playerFury;
    private final int timerInterval = 250; // скрость анимации

    public MyDraw(Context context, AttributeSet attrs) {
        super(context, attrs);

        Timer t = new Timer();
        t.start();
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.furystandart);

        int w = b.getWidth() / 4;
        int h = b.getHeight() / 2;

        Rect firstFrame = new Rect(0, 0, w, h);
        playerFury = new Sprite(firstFrame, b);

        //скорость перехода от одного слайда к другому

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                if (i == 0 && j == 3) {
                    continue;
                }
                playerFury.addFrame(new Rect(j * w, i * h, j * w + w, i * w + w));
            }
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Toast.makeText(getContext(),"I come", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        playerFury.draw(canvas);

    }


    protected void update() {
        playerFury.update(timerInterval);
        invalidate();
    }


    class Timer extends CountDownTimer {
        public Timer() {
            super(Integer.MAX_VALUE, timerInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            update();
        }

        @Override
        public void onFinish() {

        }
    }
}