package com.example.guillaume.adict_impact.vues.statsDebug;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.guillaume.adict_impact.MainActivity;
import com.example.guillaume.adict_impact.R;
import com.example.guillaume.adict_impact.modele.ObjetNotification;
import com.example.guillaume.adict_impact.modele.SacDeFrappe;
import com.example.guillaume.adict_impact.vues.GraphsActivity;

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
