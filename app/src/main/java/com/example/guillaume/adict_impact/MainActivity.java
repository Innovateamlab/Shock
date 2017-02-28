package com.example.guillaume.adict_impact;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.guillaume.adict_impact.R;
import com.example.guillaume.adict_impact.modele.SacDeFrappe;
import com.example.guillaume.adict_impact.vues.jeu.JeuVoitureActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int i=0;
    private String[] colors = new String[]{"G","B"};
    public static SacDeFrappe sacDeFrappe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onglet_home);

        sacDeFrappe = new SacDeFrappe(this);
        Button button = (Button) findViewById(R.id.jeuVoiture);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.jeuVoiture:
                Intent intent = new Intent(this, JeuVoitureActivity.class);
                startActivity(intent);
                break;
        }
    }
}