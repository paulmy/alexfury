package com.example.fury;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

public class Sprite {
    private Bitmap bitmap;
    private List<Rect> frames;
    private int currentFrame;
    private double frameTime;
    private double timeForCurrentFrame;

    public Sprite( Rect initialFrame, Bitmap bitmap) {

        this.bitmap = bitmap;
        this.frames = new ArrayList<Rect>();
        this.frames.add(initialFrame);
        this.bitmap = bitmap;
        this.timeForCurrentFrame = 0;
        this.frameTime = 0;
        this.currentFrame = 0;
    }


    public void addFrame(Rect frame) {
        frames.add(frame);
    }

    public void update(int ms) {

        timeForCurrentFrame += ms;

        if (timeForCurrentFrame >= frameTime) {
            currentFrame = (currentFrame + 1) % frames.size();
            timeForCurrentFrame = timeForCurrentFrame - frameTime;
        }


    }

    public void draw(Canvas canvas) {
        Paint p = new Paint();
        Rect destination = new Rect(0, 0, 550, 600);
        canvas.drawBitmap(bitmap, frames.get(currentFrame), destination, p);

    }



}
