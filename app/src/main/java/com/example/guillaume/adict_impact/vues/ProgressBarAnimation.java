package com.example.guillaume.adict_impact.vues;

import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by innovalab2 on 07/02/2017.
 */

public class ProgressBarAnimation extends Animation {
    private ProgressBar progressBar;
    private TextView progressTxt;
    private float from;
    private float  to;

    public ProgressBarAnimation(ProgressBar progressBar, TextView progressTxt, float from, float to) {
        super();
        this.progressBar = progressBar;
        this.progressTxt = progressTxt;
        this.from = from;
        this.to = to;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        float value = from + (to - from) * interpolatedTime;
        progressBar.setProgress((int) value);
        progressTxt.setText(String.valueOf((int) value));
    }

}
