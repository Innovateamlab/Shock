package com.example.guillaume.adict_impact;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.guillaume.adict_impact.R;
import com.example.guillaume.adict_impact.modele.SacDeFrappe;

public class MainActivity extends AppCompatActivity {

    private int i=0;
    private String[] colors = new String[]{"R","G","B"};
    private SacDeFrappe sac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onglet_home);

        sac = new SacDeFrappe(this);
        Button button = (Button) findViewById(R.id.jeuVoiture);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sac.sendData(colors[i]);
                i=(i+1)%3;
            }
        });
    }
}