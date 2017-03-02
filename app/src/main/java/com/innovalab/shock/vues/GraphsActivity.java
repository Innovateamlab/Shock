package com.innovalab.shock.vues;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYSeriesFormatter;
import com.innovalab.shock.R;
import com.innovalab.shock.modele.ObjetFrappe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ludovic on 28/02/17.
 */

public class GraphsActivity extends Activity
{
    private List<List> listeGraph= new ArrayList<List>();
    private ListView lv;
    private boolean test_graph_force;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_activity);

        Intent i = getIntent();
        final ObjetFrappe objetFrappe = (ObjetFrappe)i.getSerializableExtra("FRAPPE");

        test_graph_force = true;

        if (test_graph_force == true){
            XYSeries courbeForce = new SimpleXYSeries(objetFrappe.getTemps(),objetFrappe.getforceVecteurFrappeNum(), "Force");
            List<XYSeries> listeCourbeForce = new ArrayList<XYSeries>();
            listeCourbeForce.add(courbeForce);

            XYSeries courbeF1= new SimpleXYSeries(objetFrappe.getTemps(),objetFrappe.getforceFrappeNumC1(), "C1");
            listeCourbeForce.add(courbeF1);
            XYSeries courbeF2= new SimpleXYSeries(objetFrappe.getTemps(),objetFrappe.getforceFrappeNumC2(), "C2");
            listeCourbeForce.add(courbeF2);
            XYSeries courbeF3= new SimpleXYSeries(objetFrappe.getTemps(),objetFrappe.getforceFrappeNumC3(), "C3");
            listeCourbeForce.add(courbeF3);

            listeGraph.add(listeCourbeForce);
        }

        if(listeGraph.size() != 0){
            AffichageCourbes();
        }
    }

    private void AffichageCourbes(){
        lv = (ListView) findViewById(R.id.listViewGraph);
        lv.setAdapter(new SackOfViewsAdapter(getPlots()));
    }

    /**
     * Created by lesgourgues on 13/05/2015.
     */
    static class SackOfViewsAdapter extends BaseAdapter {

        private List<View> views = null;

        public SackOfViewsAdapter(int count) {
            super();
            views = new ArrayList<View>(count);
            for (int i = 0; i < count; i++) {
                views.add(null);
            }
        }

        public SackOfViewsAdapter(List<View> views) {
            super();
            this.views = views;
        }

        @Override
        public Object getItem(int position) {
            return (views.get(position));
        }

        @Override
        public int getCount() {
            return (views.size());
        }

        @Override
        public int getViewTypeCount() {
            return (getCount());
        }

        @Override
        public int getItemViewType(int position) {
            return (position);
        }

        @Override
        public boolean areAllItemsEnabled() {
            return (false);
        }

        @Override
        public boolean isEnabled(int position) {
            return (false);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View result = views.get(position);
            if (result == null) {
                result = newView(position, parent);
                views.set(position, result);
            }
            return (result);
        }

        @Override
        public long getItemId(int position) {
            return (position);
        }

        protected View newView(int position, ViewGroup parent) {
            throw new RuntimeException("You must override newView()!");
        }
    }

    private List<View> getPlots() {

        ArrayList<View> plots = new ArrayList<View>();
        String titre = new String();
        for (int i = 0; i < listeGraph.size(); i++) {
            if (i == 0)
                titre = "Force";
            else if (i == 1) {
                titre = "Précision";
            } else if (i == 2) {
                titre = "Vitesse";
            } else if (i == 3) {
                titre = "Position";
            }
            XYPlot pl = new XYPlot(getApplicationContext(), titre);
            ArrayList<XYSeries> serieNum = new ArrayList<XYSeries>();
            serieNum = (ArrayList<XYSeries>) listeGraph.get(i);

            //Configure toutes les couleurs du graph
            pl.getLayoutManager().remove(pl.getLegendWidget());
            pl.setPlotMargins(0, 0, 0, 0);
            pl.setPlotPadding(0, 0, 0, 0);
            pl.setGridPadding(0, 10, 10, 0);
            pl.setBackgroundColor(Color.WHITE);
            pl.getGraphWidget().getBackgroundPaint().setColor(Color.WHITE);
            pl.getGraphWidget().getGridBackgroundPaint().setColor(Color.WHITE);
            pl.getBackgroundPaint().setColor(Color.WHITE);
            pl.getTitleWidget().getLabelPaint().setColor(Color.BLACK);
            pl.getGraphWidget().getDomainLabelPaint().setColor(Color.BLACK);
            pl.getGraphWidget().getRangeLabelPaint().setColor(Color.BLACK);
            pl.getGraphWidget().getDomainOriginLabelPaint().setColor(Color.BLACK);
            pl.getGraphWidget().getDomainOriginLinePaint().setColor(Color.BLACK);
            pl.getGraphWidget().getRangeOriginLinePaint().setColor(Color.BLACK);


            for (int k = 0; k < serieNum.size(); k++) {
                XYSeries series = serieNum.get(k);
                XYSeriesFormatter format = new LineAndPointFormatter(Color.BLUE, Color.RED, null, null);
                if (series.getTitle() == "Cercle") {
                    format = new LineAndPointFormatter(Color.BLUE, null, null, null);
                }
                if (series.getTitle() == "C1") {
                    format = new LineAndPointFormatter(Color.BLUE, Color.GREEN, null, null);
                }
                if (series.getTitle() == "C2") {
                    format = new LineAndPointFormatter(Color.BLUE, Color.YELLOW, null, null);
                }
                if (series.getTitle() == "C3") {
                    format = new LineAndPointFormatter(Color.BLUE, Color.BLUE, null, null);
                }
                pl.addSeries(series, format);
            }
            pl.setMinimumHeight(300);
            plots.add(pl);
        }
        return plots;
    }
}
