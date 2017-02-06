package com.example.guillaume.adict_impact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar = null;
    NavigationView navigationView = null;
    public String[] listeprofils;
    public File[] listeprofilsJPG;
    File fichierINI = null;
    public String mode = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intentB = getIntent();
        final ObjetGlobalVar globalVar = (ObjetGlobalVar)intentB.getSerializableExtra("objVar");

        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();*/
        if (savedInstanceState == null) {
            createHome();
        }

       // navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(mode=="home") {
                super.onBackPressed();
            }
            else {
                createHome();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, ParamActivity.class);
            startActivityForResult(intent, 0);
        }

        return super.onOptionsItemSelected(item);
    }

    public void createHome() {
        HomeFragment fragment = new HomeFragment();
        // Begin a fragment transaction.
        final FragmentManager fm = getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();
        // Replace current fragment by the new one.
        ft.replace(R.id.fragment_container, fragment);
        // Commit changes.
        ft.commit();
        mode = "home";
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        /*int id = item.getItemId();
        switch(id) {
            case R.id.frapper:
                createFrapper();
                break;
            case R.id.defis:
                createDefis();
                break;
            case R.id.historique:
                createHisto();
                break;
            case R.id.top3:
                createTop3();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);*/
        return true;
    }


    public String[] getListeProfilTXT(){
        File mFile = getExternalFilesDir(null);
        File[] listeTXT = mFile.listFiles(new FiltrageTXT());
        listeprofils = new String[listeTXT.length];
        int ii=0;
        for (File f : listeTXT) {
            String nom = f.getName();
            listeprofils[ii] = nom;
            ii++;
        }
        return listeprofils;
    }

    public void setListeProfil(String[] liste){
        listeprofils = liste;
    }

    public File[] getListeProfilJPG(){
        File mFile = getExternalFilesDir(null);
        listeprofilsJPG = mFile.listFiles(new FiltrageJPG());
        return listeprofilsJPG;
    }

    public File RecherchefichierINI(String nomF){
        File mFile = getExternalFilesDir(null);
        File[] listeINI = mFile.listFiles(new FiltrageINI());
        nomF = nomF + ".ini";

        for (File f : listeINI) {
            String nom = f.getName();
            if (nom.equals(nomF)){
                fichierINI = f;
            }
        }
        return fichierINI;
    }

    ObjetProfils lecturefichierINI(Context context, File fichier){

        ObjetProfils profil = new ObjetProfils(null, null, null, null, null, null, null);

        ObjectInputStream ois = null;
        FileInputStream fis = null;
        //Lecture de la fiche
        try {
            fis = new FileInputStream(fichier);
            ois = new ObjectInputStream(fis);
            profil = (ObjetProfils) ois.readObject();
        } catch (FileNotFoundException fnfe) {
            System.out.println("Could not find file");
            fnfe.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            System.out.println("File format is wrong :(");
            cnfe.printStackTrace();
        } catch (IOException ioe) {
            System.out.println("I/O Exception while reading file");
            ioe.printStackTrace();
        } finally {
            if (fis != null) {
                safeClose(fis);
            }
        }
        return profil;
    }

    private static void safeClose(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}
class FiltrageTXT implements FilenameFilter
{

    public boolean accept(File rep, String fichier) {

        if (fichier.endsWith(".txt")) {
            return true;
        }
        else {
            return false;
        }
    }

}
class FiltrageINI implements FilenameFilter
{

    public boolean accept(File rep, String fichier) {

        if (fichier.endsWith(".ini")) {
            return true;
        }
        else {
            return false;
        }
    }

}
class FiltrageJPG implements FilenameFilter
{

    public boolean accept(File rep, String fichier) {

        if ((fichier.endsWith(".jpg")) && (fichier != "photoPro.jpg")) {
            return true;
        }
        else if (fichier == "photoPro.jpg") {
            return false;
        }
        else {
            return false;
        }
    }
}
