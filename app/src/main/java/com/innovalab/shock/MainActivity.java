package com.innovalab.shock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.innovalab.shock.R;
import com.innovalab.shock.modele.ObjetFrappe;
import com.innovalab.shock.modele.SacDeFrappe;
import com.innovalab.shock.modele.Vector3;
import com.innovalab.shock.vues.GraphsActivity;
import com.innovalab.shock.vues.jeu.JeuVoitureActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int i=0;
    private String[] colors = new String[]{"G","B"};
    private Random rand = new Random();

    public static SacDeFrappe sacDeFrappe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onglet_home);

        //sacDeFrappe = new SacDeFrappe(this);

        Button button = (Button) findViewById(R.id.jeuVoiture);
        button.setOnClickListener(this);

        button = (Button) findViewById(R.id.statsDebug);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        Intent intent;
        ObjetFrappe obj = new ObjetFrappe();
        obj.ajouterEchantillon(new Vector3(200+2*(rand.nextInt(10)+1),0,0));
        obj.calculerInfos();

        switch(v.getId())
        {
            case R.id.jeuVoiture:
                intent = new Intent(this, JeuVoitureActivity.class);
                startActivity(intent);
                break;
            case R.id.statsDebug:
                intent = new Intent(this, GraphsActivity.class);
                intent.putExtra("FRAPPE",obj);
                startActivity(intent);
                break;
        }
    }
}