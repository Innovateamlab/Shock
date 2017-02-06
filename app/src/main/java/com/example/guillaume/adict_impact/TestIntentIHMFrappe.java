package com.example.guillaume.adict_impact;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYSeriesFormatter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lesgourgues on 17/06/2015.
 */
public class TestIntentIHMFrappe extends ActionBarActivity {

    private XYPlot precision, force, vitesse;
    private TextView dataText;
    public ObjetFrappe punch;

    Toolbar toolbar = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_ihm_frappe);

        //Récupération de l'objet frappe
        Intent i = getIntent();
        punch = (ObjetFrappe) i.getSerializableExtra("frappeChoisie");
        String titre = (String) i.getSerializableExtra("phrase");
        File nomImage = (File) i.getSerializableExtra("image");

        //Pour afficher la bar d'action
        toolbar = (Toolbar) findViewById(R.id.toolbarCustom);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowHomeEnabled(false);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        LayoutInflater mInflater = LayoutInflater.from(this);
        View CustomViewBar = mInflater.inflate(R.layout.app_bar_custom, null);
        TextView mTitleTextView = (TextView) CustomViewBar.findViewById(R.id.title_text);
        ImageView mLogo = (ImageView) CustomViewBar.findViewById(R.id.imageView1);
        mTitleTextView.setText(titre);
        mTitleTextView.setTextSize(18);
        mTitleTextView.setTextColor(getResources().getColor(R.color.blanc));

        Button boutonAction = (Button)CustomViewBar.findViewById(R.id.action_btn);
        boutonAction.setVisibility(View.INVISIBLE);

        Drawable drawable = new BitmapDrawable(getResources(), nomImage.getAbsolutePath() );
        mLogo.setImageDrawable(drawable);

        toolbar.addView(CustomViewBar);
        //getSupportActionBar().setCustomView(CustomViewBar);
        //getSupportActionBar().setDisplayShowCustomEnabled(true);

        // initialisation des graph et des datas
        precision = (XYPlot) findViewById(R.id.precisionPlot);
        dataText = (TextView) findViewById(R.id.textData);
        force = (XYPlot) findViewById(R.id.forcePlot);
        vitesse = (XYPlot) findViewById(R.id.vitessePlot);

        // Données text
        String Fmax = "Force max: " + String.format("%.2f", punch.GetForceMax());//String.valueOf(punch.GetForceMax());
        String Vmax = "Vitesse maximale: " + String.format("%.2f", punch.GetVitesseMax());//String.valueOf(punch.GetVitesseMax());
        String Mmax = "Masse du corps engagé: " + String.format("%.2f", punch.GetMasseMax());//String.valueOf(punch.GetMasseMax());
        String duree = "Durée: " + String.format("%.2f", punch.GetDureems());//String.valueOf(punch.GetDureems());
        String Pmoy = "Puissance moyenne: " + String.format("%.2f", punch.GetPuissanceMoyenne());//.valueOf(punch.GetPuissanceMoyenne())
        String Newligne = System.getProperty("line.separator");

        String donneeSortie = Newligne + Fmax + Newligne + Newligne + Vmax + Newligne + Newligne + Mmax + Newligne +Newligne + duree + Newligne + Newligne + Pmoy;
        dataText.setText(donneeSortie);

        // Préparation des données de précision
        XYSeries courbeCercle = new SimpleXYSeries(GetGraphCercleX(),GetGraphCercleY(), "Cible");
        XYSeries courbePrecision = new SimpleXYSeries(GetGraphPrecisionX(punch),GetGraphPrecisionY(punch), "Point d'application de la force");
        List<XYSeries> listeCourbePrecision = new ArrayList<XYSeries>();
        listeCourbePrecision.add(courbeCercle);
        listeCourbePrecision.add(courbePrecision);
        precision = precisionGraph(listeCourbePrecision, precision);

        //Préparation de la courbes de force
        XYSeries courbeForce = new SimpleXYSeries(punch.GetTemps(),punch.GetforceVecteurFrappeNum(), "Force");
        List<XYSeries> listeCourbeForce = new ArrayList<XYSeries>();
        listeCourbeForce.add(courbeForce);
        force = precisionGraph(listeCourbeForce, force);

        //Préparation de la coube de vitesse
        XYSeries courbeVitesse = new SimpleXYSeries(punch.GetTemps(),punch.GetVitesseNum(), "Vitesse");
        List<XYSeries> listeCourbeVitesse = new ArrayList<XYSeries>();
        listeCourbeVitesse.add(courbeVitesse);
        vitesse = precisionGraph(listeCourbeVitesse, vitesse);

    }

    private XYPlot precisionGraph(List<XYSeries> listeCourbe, XYPlot graph){
        //Configure toutes les couleurs du graph
        graph.getLayoutManager().remove(graph.getLegendWidget());
        graph.getLayoutManager().remove(
                graph.getDomainLabelWidget());
        graph.setPlotMargins(1, 1, 1, 1);
        graph.setPlotPadding(0, 0, 0, 0);
        graph.setGridPadding(0, 10, 10, 0);
        graph.setBackgroundColor(Color.WHITE);
        graph.getGraphWidget().getBackgroundPaint().setColor(Color.WHITE);
        graph.getGraphWidget().getGridBackgroundPaint().setColor(Color.WHITE);
        graph.getBackgroundPaint().setColor(Color.WHITE);
        graph.getTitleWidget().getLabelPaint().setColor(Color.BLACK);
        graph.getGraphWidget().getDomainLabelPaint().setColor(Color.BLACK);
        graph.getGraphWidget().getRangeLabelPaint().setColor(Color.BLACK);
        graph.getGraphWidget().getDomainOriginLabelPaint().setColor(Color.BLACK);
        graph.getGraphWidget().getDomainOriginLinePaint().setColor(Color.BLACK);
        graph.getGraphWidget().getRangeOriginLinePaint().setColor(Color.BLACK);


        for (int k = 0; k < listeCourbe.size(); k++) {
            XYSeries series = listeCourbe.get(k);
            XYSeriesFormatter format = new LineAndPointFormatter(Color.BLUE, Color.RED, null, null);
            if (series.getTitle() == "Cible") {
                format = new LineAndPointFormatter(Color.BLUE, null, null, null);
            }
            else{
                format = new LineAndPointFormatter(Color.BLUE, Color.RED, null, null);
            }
            graph.addSeries(series, format);
        }
        graph.setMinimumHeight(300);
        return graph;
    }


    ArrayList<Number> GetGraphCercleX(){
        double rad;
        Number[] x = new Number[39];
        ArrayList<Number> alX = new ArrayList<Number>();
        for (int j = 0; j < 39; j++){
            rad = (j*10*Math.PI)/180;
            x[j]=6*Math.cos(rad);
            alX.add(x[j]);
        }
        return alX;
    }

    ArrayList<Number> GetGraphCercleY(){
        double rad;
        Number[] y = new Number[39];
        ArrayList<Number> alY = new ArrayList<Number>();
        for (int j = 0; j < 39; j++){
            rad = (j*10*Math.PI)/180;
            y[j]= 6*(Math.sin(rad));
            alY.add(y[j]);
        }
        return alY;
    }

    ArrayList<Number> GetGraphPrecisionX(ObjetFrappe laFrappe){
        ArrayList<Number> prX = new ArrayList<Number>();
        float precisionX = (laFrappe.GetPrecisionDX()).floatValue();
        prX.add(precisionX);
        return prX;
    }

    ArrayList<Number> GetGraphPrecisionY(ObjetFrappe laFrappe){
        ArrayList<Number> prY = new ArrayList<Number>();
        float precisionY = (laFrappe.GetPrecisionDY()).floatValue();
        prY.add(precisionY);
        return prY;
    }

}

