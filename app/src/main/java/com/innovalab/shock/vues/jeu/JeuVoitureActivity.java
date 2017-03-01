package com.innovalab.shock.vues.jeu;

import android.animation.Animator;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.innovalab.shock.MainActivity;
import com.innovalab.shock.R;
import com.innovalab.shock.modele.ObjetFrappe;
import com.innovalab.shock.modele.ObjetNotification;
import com.innovalab.shock.modele.SacDeFrappe;
import com.innovalab.shock.vues.ProgressBarAnimation;

import java.util.Observable;
import java.util.Observer;

public class JeuVoitureActivity extends AppCompatActivity implements Observer {

    ImageView j1;
    ImageView j2;
    ImageView end;

    TextView j1_icon;
    TextView j2_icon;

    TextView win1;
    TextView win2;

    Button restart = null;

    ProgressBar progressBar1;
    ProgressBar progressBar2;

    TextView progressTxt1;
    TextView progressTxt2;

    float dest1 = 0, dest2=0;

    float rot = 0;

    // position de départ des voitures
    float pos_start1 = 902;
    float pos_start2 = 902;

    // valeur max renvoyée par la carte à l'application
    float max = 500;

    // distance restant à parcourir pour les voitures
    float dist_remain1 = 0, dist_remain2 = 0;

    float dest_tmp;

    ImageView j_active;
    TextView j_icon_active;
    TextView j_icon_inactive;
    String send_data;
    float dest;
    ProgressBar progressBar_active;
    ProgressBar progressBar_inactive;
    TextView progressTxt_active;
    TextView progressTxt_inactive;
    TextView win;



    private TextView affichageresultat;

    private final static int FRAME_RATE = 30;
    private final static int LIFETIME = 500;
    private Handler mHandler;
    private View mFX;
    private Explosion mExplosion;

    private SacDeFrappe sacDeFrappe;
    private ObjetFrappe objetFrappe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jeu_voiture);

        sacDeFrappe = MainActivity.sacDeFrappe;
        sacDeFrappe.addObserver(this);

        // voitures
        j1 = (ImageView) findViewById(R.id.j1);
        j2 = (ImageView) findViewById(R.id.j2);

        // icônes "joueur 1" et "joueur 2"
        j1_icon = (TextView) findViewById(R.id.j1_icon);
        j2_icon = (TextView) findViewById(R.id.j2_icon);
        j2_icon.setVisibility(View.INVISIBLE);


        // ligne d'arrivée
        end = (ImageView) findViewById(R.id.end);

        // messages fin de partie
        win1 = (TextView) findViewById(R.id.win1);
        win1.setVisibility(View.INVISIBLE);
        win2 = (TextView) findViewById(R.id.win2);
        win2.setVisibility(View.INVISIBLE);

        // barres de progression
        progressBar1 = (ProgressBar) findViewById(R.id.force1);
        progressBar2 = (ProgressBar) findViewById(R.id.force2);

        progressTxt1 = (TextView) findViewById(R.id.force1_txt);
        progressTxt2 = (TextView) findViewById(R.id.force2_txt);

        // bouton restart
        restart = (Button) findViewById(R.id.restart_btn);

        affichageresultat = (TextView) findViewById(R.id.affichageresultat);

        restart();


        // effet de particules
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.frame);
        mFX = new View(this) {
            @Override
            protected void onDraw(Canvas c) {
                if (mExplosion!=null && !mExplosion.isDead())  {
                    mExplosion.update(c);
                    mHandler.removeCallbacks(mRunner);
                    mHandler.postDelayed(mRunner, FRAME_RATE);
                } else if (mExplosion!=null && mExplosion.isDead()) {
                }
                super.onDraw(c);
            }
        };
        mFX.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        layout.addView(mFX);
        mHandler = new Handler();

    }

    private Runnable mRunner = new Runnable() {
        @Override
        public void run() {
            mHandler.removeCallbacks(mRunner);
            mFX.invalidate();
        }
    };

    // animation sur j1 ou j2
    public void avanceAnimation(View v)
    {
        if (j1_icon.getVisibility() == View.VISIBLE) {
            j_active = j1;
            j_icon_active = j1_icon;
            j_icon_inactive = j2_icon;
            progressBar_active = progressBar1;
            progressBar_inactive = progressBar2;
            progressTxt_active = progressTxt1;
            progressTxt_inactive = progressTxt2;
            send_data = "B";
            dest = dest1;
        } else {
            j_active = j2;
            j_icon_active = j2_icon;
            j_icon_inactive = j1_icon;
            progressBar_active = progressBar2;
            progressBar_inactive = progressBar1;
            progressTxt_active = progressTxt2;
            progressTxt_inactive = progressTxt1;
            send_data = "G";
            dest = dest2;
        }

        dest_tmp = objetFrappe.getPuissanceMax();
        if (dest_tmp != -1) {
            dest += 250*(dest_tmp/max);
            restart.setOnClickListener(null);
            final ViewPropertyAnimator animation1 = j_active.animate().translationY(-dest).setInterpolator(new LinearInterpolator()).setDuration(1500);

            progressTxt_active.setText("0");
            progressBar_active.setProgress(0);
            ProgressBarAnimation anim = new ProgressBarAnimation(progressBar_active, progressTxt_active, 0, 100*(dest_tmp/max));
            anim.setDuration(1500);
            progressBar_active.startAnimation(anim);

            int[] loc = new int[2];
            j_active.getLocationOnScreen(loc);
            int offsetX = loc[0];
            int offsetY = (int) (loc[1] + (j_active.getHeight() * 0.5) + 40);
            mExplosion = new Explosion((int) (dest_tmp/4), offsetX, offsetY, v.getContext(), j_active);
            mHandler.removeCallbacks(mRunner);
            mHandler.post(mRunner);
            v.animate().setDuration(LIFETIME).start();


            animation1.setListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationEnd(final Animator animation) {
                    if (j1_icon.getVisibility() == View.VISIBLE) {
                        dest1 = dest;
                    } else {
                        dest2 = dest;
                    }

                    j_icon_active.setVisibility(View.INVISIBLE);
                    j_icon_inactive.setVisibility(View.VISIBLE);


                    restart.setOnClickListener(restartListener);

                    winAnimation();
                }

                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    }

    public void winAnimation()
    {
        dist_remain1 = (j1.getTop() - (end.getTop() + end.getHeight())) - dest1;
        dist_remain2 = (j2.getTop() - (end.getTop() + end.getHeight())) - dest2;
        if (dist_remain1 <= -8 || dist_remain2 <= -8) {
            if (dist_remain1 <= -8) {
                win = win1;
                send_data = "B";
            } else {
                win = win2;
                send_data = "G";
            }

            win.setVisibility(View.VISIBLE);
            restart.setOnClickListener(null);
            final ViewPropertyAnimator animation1 = win.animate().rotation(rot).setInterpolator(new LinearInterpolator()).setDuration(3000);

            animation1.setListener(new Animator.AnimatorListener() {


                @Override
                public void onAnimationEnd(final Animator animation) {
                    win.setVisibility(View.INVISIBLE);
                    restart();

                }

                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        else {
            sacDeFrappe.sendData(send_data);
        }


    };

    // Uniquement pour le bouton "restart"
    protected View.OnClickListener restartListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            restart();
        }
    };

    public void restart() {
        j1.setY(pos_start1);
        j2.setY(pos_start2);

        dest1 = 0;
        dest2 = 0;

        j1_icon.setVisibility(View.VISIBLE);
        j2_icon.setVisibility(View.INVISIBLE);

        progressBar1.setProgress(0);
        progressBar2.setProgress(0);

        progressTxt1.setText("0");
        progressTxt2.setText("0");

        affichageresultat.setText("");

        restart.setOnClickListener(restartListener);

        sacDeFrappe.sendData("A");
        sacDeFrappe.sendData("G");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public void onBackPressed() {
        sacDeFrappe.deleteObserver(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void update(Observable observable, Object data)
    {
        ObjetNotification objNotification = (ObjetNotification) data;
        switch(objNotification.label)
        {
            case OBJET_FRAPPE:
                objetFrappe = objNotification.getObjetFrappe();
                break;
        }

        avanceAnimation(mFX);
    }
}
