package com.example.guillaume.adict_impact.modele;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ludovic on 27/02/17.
 */

public class ObjetFrappe {

    //La précision temportelle sera définie dans la carte (durée entre deux échantillons de la liste)
    private List<Vector3> m_capteurs;
    private float m_puissanceMax, m_puissanceMoyenne;

    public ObjetFrappe() {
        reset();
    }

    /**
     * @brief Permet d'effectuer les calculs d'analyse à partir des données des capteurs.
     */
    public void calculerInfos() {
        m_puissanceMax = m_capteurs.get(0).x;
    }

    public float getPuissanceMax(){return m_puissanceMax;}

    /**
     * @brief Initialise les données membres à partir des informations de la carte.
     */
    public void ajouterEchantillonParser(String infosCarte) {
        m_capteurs.add(new Vector3(Integer.parseInt(infosCarte),0,0));
    }

    public void ajouterEchantillon(Vector3 vector){
        m_capteurs.add(vector);
    }

    public void reset()
    {
        m_capteurs = new ArrayList<>();
    }

    public class Vector3{
        public float x, y, z;
        public Vector3(float x,float y,float z) {this.x=x;this.y=y;this.z=z;}
    }
}
