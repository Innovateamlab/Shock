package com.example.guillaume.adict_impact.vues.jeu;

/**
 * Created by innovalab2 on 09/02/2017.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.widget.ImageView;

import com.example.guillaume.adict_impact.R;

public class Particle {

    public static final int ALIVE = 0;
    public static final int DEAD = 1;
    private int mState;
    private Bitmap mBitmap;
    private float mX, mY;
    private double mXV, mYV;
    private float mAge;
    private float mLifetime;
    private Paint mPaint;
    private int mAlpha;
    private static Bitmap mBase;

    private ImageView J1;

    int n=0;

    private double randomDouble(double min, double max) {
        return min + (max - min) * Math.random();
    }

    public boolean isAlive() {
        return mState == ALIVE;
    }

    public Particle(int x, int y, int lifetime, int maxSpeed, int maxScale, Context c, ImageView j1) {
        mX = x;
        mY = y;
        J1 = j1;
        mState = ALIVE;
        if (mBase==null) {
            mBase = BitmapFactory.decodeResource(c.getResources(), R.drawable.fire_fx);
        }
        int newWidth = (int) (mBase.getWidth()*randomDouble(1.01, maxScale));
        int newHeight = (int) (mBase.getHeight()*randomDouble(1.01, maxScale));
        mBitmap = Bitmap.createScaledBitmap(mBase, newWidth, newHeight, true);
        mLifetime = lifetime;
        mAge = 0;
        mAlpha = 0xff;
        mXV = (randomDouble(0, maxSpeed * 2) - maxSpeed);
        mYV = (randomDouble(0, maxSpeed ));
        mPaint = new Paint();
    }

    public void update() {
        if (mState != DEAD) {
            int[] loc = new int[2];
            J1.getLocationOnScreen(loc);
            mY = (float) (loc[1]+ (J1.getHeight()*0.5) + 40) + (float) (mAge*mYV);
            if (mAlpha <= 0) {
                mState = DEAD;
            } else {
                mAge++;
                float factor = (mAge/mLifetime) * 2;
                mAlpha = (int) (0xff - (0xff * factor));
                mPaint.setAlpha(mAlpha);
            }
            if (mAge >= mLifetime) {
                mState = DEAD;
            }
        }
        n = n+1;
        Log.i("nboccurences", String.valueOf(n));
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(mBitmap, mX, mY, mPaint);
    }

}
