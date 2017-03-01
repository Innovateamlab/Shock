package com.innovalab.shock.vues.statsDebug;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.innovalab.shock.MainActivity;
import com.innovalab.shock.R;
import com.innovalab.shock.modele.ObjetNotification;
import com.innovalab.shock.modele.SacDeFrappe;
import com.innovalab.shock.vues.GraphsActivity;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by ludovic on 28/02/17.
 */

public class DebugActivity extends Activity implements Observer
{
    private SacDeFrappe sacDeFrappe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.debug_activity);

        sacDeFrappe = MainActivity.sacDeFrappe;
        sacDeFrappe.addObserver(this);

        reset();
    }

    @Override
    public void onBackPressed() {
        sacDeFrappe.deleteObserver(this);
    }

    public void reset()
    {
        sacDeFrappe.sendData("A");
        sacDeFrappe.sendData("G");
    }

    @Override
    public void update(Observable observable, Object data)
    {
        ObjetNotification objNotification = (ObjetNotification) data;
        Intent intent = new Intent(this, GraphsActivity.class);
        intent.putExtra("FRAPPE",objNotification.getObjetFrappe());
        startActivity(intent);
        reset();
    }
}
