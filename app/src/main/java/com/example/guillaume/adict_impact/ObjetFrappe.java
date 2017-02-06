package com.example.guillaume.adict_impact;

import android.content.SharedPreferences;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.abs;

/**
 * Created by lesgourgues on 31/03/2015.
 */
public class ObjetFrappe implements Serializable {

    //Préférences partagées
    private SharedPreferences mPrefs;

    private Double[] pforceCapteur_1, pforceCapteur_2, pforceCapteur_3, pforceVecteurFrappe, pvitesse, pmasse, pposition, Temps;
    private Double duree, forceMax, masseMax, dureems, F1min, F2min, F3min, plusContact;
    private Double vitesseMax, energieC, Tgainage, positionMax;
    private String stringD, stringFM, stringVM, stringMM, parme;
    private int pnbrePts;
    private Date pmaDateDeFrappe;
    private Boolean testD, testFM, testMM, testVM;
    static private SimpleDateFormat dateFormatMoche = new SimpleDateFormat("yyyyMMdd.HHmmss");
    private Double precisionX, precisionY;

    private static Double dT=6.4528e-04;//210E-6;
    private static Double gainage = 0.05; //gainage de la main et du poignet r�alis� si la force est a 5% de la force max

    public ObjetFrappe(Double[] forceCapteur_1, Double[] forceCapteur_2, Double[] forceCapteur_3, Double[] forceVecteurFrappe, Double[] vitesse, Double[] position, Double[] masse, Date maDateDeFrappe, String arme){ //, double[] position){

        pforceCapteur_1 = forceCapteur_1;
        pforceCapteur_2 = forceCapteur_2;
        pforceCapteur_3 = forceCapteur_3;
        pforceVecteurFrappe = forceVecteurFrappe;
        pvitesse = vitesse;
        pmasse = masse;
        pmaDateDeFrappe = maDateDeFrappe;
        pposition = position;
        parme = arme;
    }

    String GetString(){

        String string = new String("force max ");//+ forceMax );
        return string;
    }

    void SetForceVecteurFrappe(Double[] forceVecteurFrappe){
        pforceVecteurFrappe = forceVecteurFrappe;
    }

    void SetForceCapteur_1(Double[] forceCapteur_1){
        pforceCapteur_1 = forceCapteur_1;
    }

    void SetForceCapteur_2(Double[] forceCapteur_2){
        pforceCapteur_2 = forceCapteur_2;
    }

    void SetForceCapteur_3(Double[] forceCapteur_3){
        pforceCapteur_3 = forceCapteur_3;
    }

    void SetVitesse(Double[] vitesse){
        pvitesse = vitesse;
    }

    void SetMasse(Double[] masse){
        pmasse = masse;
    }

    void SetPosition(Double[] position){
        pposition = position;
    }

    void SetDate(Date maDateDeFrappe){
        pmaDateDeFrappe = maDateDeFrappe;
    }

    void SetArme (String arme) {
        parme = arme;
    }

    Double[] GetForceVecteurFrappe(){
        return pforceVecteurFrappe;
    }

    Double[] GetForceCapteur_1(){
        return pforceCapteur_1;
    }

    Double[] GetForceCapteur_2(){
        return pforceCapteur_2;
    }

    Double[] GetForceCapteur_3(){
        return pforceCapteur_3;
    }

    Double[] GetVitesse(){
        return pvitesse;
    }

    Double[] GetPosition(){
        return pposition;
    }

    Double[] GetMasse(){
        return pmasse;
    }

    Date GetDate(){
        return pmaDateDeFrappe;
    }

    String GetArme (){
        return parme;
    }
    //Retourne la date + l'heure en double (AAAAMMJJ.HHMMSS)
    public Double GetDateDouble()  {
        String dateString = dateFormatMoche.format(pmaDateDeFrappe);
        System.out.println("date : " + dateString);
        Double dateDouble = Double.parseDouble(dateString);
        return dateDouble;
    }

    //Retourne la force max de la frappe
    Double GetForceMax(){
        forceMax = 0.0;
        for (int p=1; p <= GetNbrePts()-1; p++)
        {
            if ( pforceVecteurFrappe[p] > forceMax ) forceMax = pforceVecteurFrappe[p];//(int)
        }

        return forceMax;
    }

    //Retourne la force max de la frappe
    Double GetVitesseMax(){
        vitesseMax = 0.0;
        for (int p=0; p <= GetNbrePts()-1; p++)
        {
            if ( pvitesse[p] > vitesseMax ) vitesseMax = pvitesse[p];
        }
        Double pow = Math.pow(10, 3);
        vitesseMax = (Math.floor(vitesseMax * pow)) / pow;
        return vitesseMax;
    }

    //Retourne la force max de la frappe
    Double GetPositionMax(){
        positionMax = 0.0;
        for (int p=0; p <= GetNbrePts()-1; p++)
        {
            if ( pposition[p] > positionMax ) positionMax = pposition[p];
        }
        Double pow = Math.pow(10, 3);
        //positionMax = (Math.floor(positionMax * pow)) / pow;
        return positionMax;
    }

    Double GetMasseMax(){
        masseMax = 0.0;
        for (int p=0; p <= GetNbrePts()-1; p++)
        {
            if ( pmasse[p] > masseMax ) masseMax = pmasse[p];
        }
        Double pow = Math.pow(10, 1);
        masseMax = (Math.floor(masseMax * pow)) / pow;
        return masseMax;
    }

    ArrayList<Number> GetTemps(){
        Number[] numberTempsFrappe = new Number[GetNbrePts()];
        ArrayList<Number> alTemps = new ArrayList<Number>();
        for (int j = 0; j< GetNbrePts();j++) {
            numberTempsFrappe[j] = j*dT*1000;
            alTemps.add(numberTempsFrappe[j]);
        }
        return alTemps;
    }



    ArrayList<Number> GetTempsFrappe(){
        int intStop = (int)((this.GetDuree())/dT);
        if (intStop >= pvitesse.length) {
            intStop = pvitesse.length;
        }
        Number[] numberTempsFrappe = new Number[intStop];
        ArrayList<Number> alTempsF = new ArrayList<Number>();
        for (int jj = 0; jj < intStop-1;jj++) {
            numberTempsFrappe[jj] = jj*dT*1000;
            alTempsF.add(numberTempsFrappe[jj]);
        }
        return alTempsF;
    }
    Double[] GetforceVecteurFrappe(){

        return pforceVecteurFrappe;
    }

    ArrayList<Number> GetforceVecteurFrappeNum(){
        ArrayList<Number> alVecteurFrappe = new ArrayList<Number>();
        for (int j = 0; j< GetNbrePts();j++) {
            alVecteurFrappe.add(pforceVecteurFrappe[j]);
        }
        return alVecteurFrappe;
    }

    ArrayList<Number> GetforceFrappeNumC1(){
        ArrayList<Number> alCap1 = new ArrayList<Number>();
        for (int j = 0; j< GetNbrePts();j++) {
            alCap1.add(pforceCapteur_1[j]);
        }
        return alCap1;
    }

    ArrayList<Number> GetforceFrappeNumC2(){
        ArrayList<Number> alCap2 = new ArrayList<Number>();
        for (int j = 0; j< GetNbrePts();j++) {
            alCap2.add(pforceCapteur_2[j]);
        }
        return alCap2;
    }

    ArrayList<Number> GetforceFrappeNumC3(){
        ArrayList<Number> alCap3 = new ArrayList<Number>();
        for (int j = 0; j< GetNbrePts();j++) {
            alCap3.add(pforceCapteur_3[j]);
        }
        return alCap3;
    }

    ArrayList<Number> GetVitesseNum(){
        ArrayList<Number> alVit = new ArrayList<Number>();
        for (int j = 0; j< GetNbrePts();j++) {
            alVit.add(pvitesse[j]);
        }
        return alVit;
    }

    ArrayList<Number> GetPositionNum(){
        ArrayList<Number> alPos = new ArrayList<Number>();
        for (int j = 0; j< GetNbrePts();j++) {
            alPos.add(pposition[j]*1000);
        }
        return alPos;
    }

    int GetNbrePts(){
        pnbrePts = pforceVecteurFrappe.length;
        return pnbrePts;
    }

    ArrayList<Number> GetDelta(){

        int indPositionMax = this.GetNbrePts()-1;
        double plusContact = 10000;
        Double[] deltaAA = new Double[GetNbrePts()];
        Double[] A = new Double[GetNbrePts()];
        Double[] AA = new Double[GetNbrePts()];

        for(int ii = 0; ii < GetNbrePts()-1; ii++){
            A[ii] = 0.0;
            AA[ii] = 0.0;
            deltaAA[ii] = 0.0;
        }
        for(int ii = 1; ii < GetNbrePts()-2; ii++){
            A[ii] = (pposition[ii+1] - pposition[ii-1])/(2*dT);
            AA[ii] = (A[ii+1] - A[ii-1])/(2*dT);
        }

        for(int ii = 0; ii < this.GetNbrePts()-2; ii++){
            Double toto = this.GetPositionMax();
            if (pposition[ii] == toto){
                indPositionMax = ii;
            }
            deltaAA[ii] = abs(AA[ii]) - abs(AA[ii+1]);
        }

        //deltaAA[199] = 0.0;

        for(int ii = indPositionMax+30; ii < this.GetNbrePts()-1; ii++){
            if ((abs(deltaAA[ii]) > 400)&&(plusContact == 10000)) {
                plusContact = ii;
            }
        }

        ArrayList<Number> alDel = new ArrayList<Number>();
        for (int j = 0; j< GetNbrePts();j++) {
            alDel.add(deltaAA[j]);
        }
        duree = plusContact * dT;
        return alDel;
    }

    Double GetDuree(){

        int indPositionMax = this.GetNbrePts()-1;
        double plusContact = 10000;
        Double[] deltaAA = new Double[GetNbrePts()];
        Double[] A = new Double[GetNbrePts()];
        Double[] AA = new Double[GetNbrePts()];

        int stop = 0;
        int pasPlus = 1;
        Double[] nana = this.GetForceVecteurFrappe();
        for(int ii = 0; ii <= GetNbrePts()-1; ii++){
            A[ii] = 0.0;
            AA[ii] = 0.0;
            deltaAA[ii] = 0.0;
            //Double nana = this.pforceVecteurFrappe[ii];
            if ((nana[ii] <= 0.0) && (pasPlus == 1)){
                stop = ii;
                pasPlus = 0;
            }
        }
        if ((stop >= 197) && (pasPlus == 1)){
            stop = 195;
        }
        for(int ii = 1; ii <= stop; ii++){
            A[ii] = (pposition[ii+1] - pposition[ii-1])/(2*dT);
            AA[ii] = (A[ii+1] - A[ii-1])/(2*dT);
        }
        Double toto = this.GetPositionMax();
        for(int ii = 0; ii <= stop; ii++){

            if (pposition[ii] == toto){
                indPositionMax = ii;
            }
            deltaAA[ii] = abs(AA[ii]) - abs(AA[ii+1]);
        }

        //deltaAA[199] = 0.0;
        int Tmax = indPositionMax+30;
        for(int ii = Tmax; ii <= stop; ii++){
            if ((abs(deltaAA[ii]) > 400.0)&&(plusContact == 10000)) {
                plusContact = ii;
            }
        }

        if (plusContact == 10000){
            plusContact = stop;
        }
        duree = plusContact * dT;
        return duree;
    }

    Double GetDureems(){
        dureems = GetDuree() *1000;
       /* Double pow = Math.pow(10, 1);
        dureems = (Math.floor(dureems * pow)) / pow;*/
        return dureems;
    }


    Double[] GetPuissanceInstantane(Double[] forceVecteurFrappe, Double[] vitesse){
        int intStop = (int)((GetDuree())/dT);
        if (intStop >= vitesse.length) {
            intStop = vitesse.length;
        }
        Double[] puissanceInstantane = new Double[intStop];
        for (int pp=0; pp < intStop-1; pp++){
            puissanceInstantane[pp] = forceVecteurFrappe[pp] * vitesse[pp];
        }
        return puissanceInstantane;
    }

    ArrayList<Number>  GetPuissanceInstantaneNum(){
        int intStop = (int)((this.GetDuree())/dT);
        Double[] puissanceInstantane = this.GetPuissanceInstantane(this.GetForceVecteurFrappe(), this.GetVitesse() );
        if (intStop >= pvitesse.length) {
            intStop = pvitesse.length;
        }
        ArrayList<Number> alPui = new ArrayList<Number>();
        for (int pp=0; pp < intStop-1; pp++){
            alPui.add(puissanceInstantane[pp]);
        }
        return alPui;
    }

    Double GetPuissanceMoyenne(){
        Double[] puissanceInst = this.GetPuissanceInstantane(this.GetForceVecteurFrappe(), this.GetVitesse());
        int intStop = (int)((this.GetDuree())/dT);
        if (intStop >= pvitesse.length) {
            intStop = pvitesse.length;
        }
        Double puissanceMoyenne = 0.0;
        for (int pp=0; pp < intStop-1; pp++){
            puissanceMoyenne = puissanceMoyenne + puissanceInst[pp];
        }

        puissanceMoyenne = puissanceMoyenne/(this.GetDuree());
        return puissanceMoyenne;


    }

    Double GetPrecisionDX(){
        Double Xa= 0.0, Xc = -3.4641, Xb = 3.4641;  //pour un diamètre de 8cm
        precisionX = 0.0;
        Double[] F1 = this.GetForceCapteur_1();
        Double[] F2 = this.GetForceCapteur_2();
        Double[] F3 = this.GetForceCapteur_3();
        Double[] F = this.GetForceVecteurFrappe();
        Double[] m1 = new Double[this.GetNbrePts()];
        Double[] m2 = new Double[this.GetNbrePts()];
        Double[] m3 = new Double[this.GetNbrePts()];
        Double P = 0.0;
        for (int ii = 1; ii < (this.GetNbrePts()-2)/2; ii++){
            m1[ii] = F1[ii]/F[ii];
            m2[ii] = F2[ii]/F[ii];
            m3[ii] = F3[ii]/F[ii];
            P = (-m1[ii] * Xa -m2[ii] * Xb -m3[ii] * Xc)/(m1[ii] + m2[ii] + m3[ii]);
            precisionX = precisionX + P;
        }
        precisionX = precisionX/(this.GetNbrePts()-2);//1.5;//
        return precisionX;
    }

    Double GetPrecisionDY(){
        Double Ya= -4.0, Yb = 2.0, Yc = 2.0;  //pour un diamètre de 8cm
        precisionY = 0.0;
        Double[] F1 = this.GetForceCapteur_1();
        Double[] F2 = this.GetForceCapteur_2();
        Double[] F3 = this.GetForceCapteur_3();
        Double[] F = this.GetForceVecteurFrappe();
        Double[] m1 = new Double[this.GetNbrePts()];
        Double[] m2 = new Double[this.GetNbrePts()];
        Double[] m3 = new Double[this.GetNbrePts()];
        Double P = 0.0;
        for (int ii = 1; ii < (this.GetNbrePts()-2)/2; ii++){
            m1[ii] = F1[ii]/F[ii];
            m2[ii] = F2[ii]/F[ii];
            m3[ii] = F3[ii]/F[ii];
            P = (-m1[ii] * Ya -m2[ii] * Yb -m3[ii] * Yc)/(m1[ii] + m2[ii] + m3[ii]);
            precisionY = precisionY + P;
        }
        precisionY = precisionY/(this.GetNbrePts()-2);//2.0;//
        return precisionY;
    }

    Double GetMin1F(){
        F1min = Collections.min(Arrays.asList(pforceCapteur_1));
        return F1min;
    }
    Double GetMin2F(){
        F2min = Collections.min(Arrays.asList(pforceCapteur_2));
        return F2min;
    }
    Double GetMin3F(){
        F3min = Collections.min(Arrays.asList(pforceCapteur_3));
        return F3min;
    }



}

