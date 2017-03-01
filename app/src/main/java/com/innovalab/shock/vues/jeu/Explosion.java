package com.innovalab.shock.vues.jeu;

/**
 * Created by innovalab2 on 09/02/2017.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.widget.ImageView;

public class Explosion {

    public static final int ALIVE 	= 0;
    public static final int DEAD 	= 1;
    private final static int LIFETIME = 150;
    private final static int MAX_SCALE = 1;
    private final static int MAX_SPEED = 3;

    int n=0;

    private Particle[] mParticles;
    private int mState;

    public Explosion(int numberOfParticles, int x, int y, Context c, ImageView j1) {
        mState = ALIVE;
        mParticles = new Particle[numberOfParticles];
        for (int i = 0; i < mParticles.length; i++) {
            Particle p = new Particle(x + (int)(50*Math.random()-25), y + (int)(10*Math.random()), LIFETIME, MAX_SPEED, MAX_SCALE, c, j1);
            mParticles[i] = p;
        }
    }

    public boolean isDead() {
        return mState == DEAD;
    }

    public void update(Canvas canvas) {
        if (mState != DEAD) {
            boolean isDead = true;
            for (int i = 0; i < mParticles.length; i++) {
                if (mParticles[i].isAlive()) {
                    mParticles[i].update();
                    isDead = false;
                }
            }
            if (isDead)
                mState = DEAD;
        }
        draw(canvas);
        n = n+1;
    }

    public void draw(Canvas canvas) {
        for(int i = 0; i < mParticles.length; i++) {
            if (mParticles[i].isAlive()) {
                mParticles[i].draw(canvas);
            }
        }
    }
}
