package com.example.guillaume.adict_impact;

import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.io.Serializable;

/**
 * Created by lesgourgues on 03/06/2015.
 */
public class ObjetProfils implements Serializable {
    File[] plisteTXT = null;
    File[] plisteINI = null;
    File[] plisteJPG = null;
    String pnom = "", pprenom = "", panniv = "", ptaille = "", pparcour = "", pSexe = "", ppassword = "";

    public ObjetProfils(String nom, String prenom,String anniv,String taille, String parcour, String Sexe, String password){//File[] listeTXT, File[] listeINI, File[] listeJPG){

        pnom = nom;
        pprenom = prenom;
        panniv = anniv;
        ptaille = taille;
        pparcour = parcour;
        pSexe = Sexe;
        ppassword = password;

       /* File tmpFile =  Environment.getExternalStorageDirectory();//Context.getExternalFilesDir(null);//getExternalFilesDir(null);Environment.getExternalStorageDirectory();//
        File mFile = new File(tmpFile + "/Android/data/com.exemple.lesgourgues.adict_impact_V21/files");

        if (mFile != null) {

            plisteTXT = mFile.listFiles(new FiltrageTXT());
            plisteINI = mFile.listFiles(new FiltrageINI());
            plisteJPG = mFile.listFiles(new FiltrageJPG());
        }
        /*plisteTXT = listeTXT;
        plisteINI = listeINI;
        plisteJPG = listeJPG;*/
    }

    public String getPnom(){
        return pnom;
    }
    public String getPprenom(){
        return pprenom;
    }
    public String getPanniv(){
        return panniv;
    }
    public String getPtaille(){
        return ptaille;
    }
    public String getPparcour(){
        return pparcour;
    }
    public String getPsexe(){
        return pSexe;
    }
    public String getPpassword() {
        return ppassword;
    }

    public void setPnom (String nom){
        pnom = nom;
    }

    public void setPprenom(String prenom){
        pprenom = prenom;
    }

    public void setPanniv(String anniv){
        panniv = anniv;
    }

    public void setPtaille(String taille){
        ptaille = taille;
    }

    public void setPparcour(String parcour){
        pparcour = parcour;
    }

    public void setPsexe(String sexe){
        pSexe = sexe;
    }
    public void setPpassword(String password){
        ppassword = password;
    }
/*
    public File[] getListeTXT(){
        return plisteTXT;
    }

    public File[] getListeINI(){
        return plisteTXT;
    }

    public File[] getListeJPG(){
        return plisteTXT;
    }

    public void setListeTXT(File[] listeTXT){
        plisteTXT = listeTXT;
    }

    public void setListeINI(File[] listeINI){
        plisteINI = listeINI;
    }

    public void setListeJPG(File[] listeJPG){
        plisteJPG = listeJPG;
    }

    public String getString(){
        String s = "toto est grand";
        return s;
    }
    public String getListeNom(){
        File test = null;//plisteTXT[0];

        String listeNom = test.toString();
        return listeNom;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
*/

}
