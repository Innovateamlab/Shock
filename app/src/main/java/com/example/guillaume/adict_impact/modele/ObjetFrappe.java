package com.example.guillaume.adict_impact.modele;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ludovic on 27/02/17.
 */

public class ObjetFrappe implements Serializable{

    //La précision temportelle sera définie dans la carte (durée entre deux échantillons de la liste)
    private List<Vector3> m_capteurs;
    private List<Float> m_temps, m_puissances, m_c1, m_c2, m_c3;
    private float m_puissanceMax, m_puissanceMoyenne;

    public ObjetFrappe() {
        reset();
    }

    /**
     * @brief Permet d'effectuer les calculs d'analyse à partir des données des capteurs.
     */
    public void calculerInfos()
    {
        m_puissanceMax = m_capteurs.get(0).x;

        //TODO : Enlever ça
        calculsDebug();
    }

    //TODO : Enlever ça
    private void calculsDebug()
    {
        int nbEchantillons = 20;

        float max1 = m_puissanceMax, max2 = 0.9f*m_puissanceMax, max3 = 1.2f*m_puissanceMax;
        reset();

        for(int i=0;i<nbEchantillons;i++)
        {
            int diff = Math.abs((nbEchantillons/2)-i);
            Vector3 vect3;
            if(diff==0)
            {
                vect3 = new Vector3(max1,max2,max3);
            }
            else
            {
                vect3 = new Vector3((1-(diff/20.0f))*max1,(1-(diff/20.0f))*max2,(1-(diff/20.0f))*max3);

            }

            Log.i("Debug",i+" "+diff+" "+vect3);

            m_capteurs.add(vect3);
            m_puissances.add((vect3.x+vect3.y+vect3.z)/3);
            m_temps.add((float)i);
            m_c1.add(vect3.x);
            m_c2.add(vect3.y);
            m_c3.add(vect3.z);
        }
    }

    public float getPuissanceMax(){return m_puissanceMax;}
    public List<Float> getTemps(){return m_temps;}
    public List<Float> getforceVecteurFrappeNum(){return m_puissances;}

    public List<Float> getforceFrappeNumC1(){return m_c1;}
    public List<Float> getforceFrappeNumC2(){return m_c2;}
    public List<Float> getforceFrappeNumC3(){return m_c3;}


    /**
     * @brief Initialise les données membres à partir des informations de la carte.
     */
    public void ajouterEchantillonParser(String infosCarte) {
        m_capteurs.add(new Vector3(Integer.parseInt(infosCarte),0,0));
    }

    public void ajouterEchantillon(Vector3 vector){
        m_capteurs.add(vector);
    }

    public void reset() {
        m_capteurs = new ArrayList<>();
        m_temps = new ArrayList<>();
        m_puissances = new ArrayList<>();
        m_c1 = new ArrayList<>();
        m_c2 = new ArrayList<>();
        m_c3 = new ArrayList<>();
    }
}
