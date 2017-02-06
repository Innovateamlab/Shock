package com.example.guillaume.adict_impact;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lesgourgues on 02/04/2015.
 */
public class ObjetCompetition implements Serializable {
    private  int pintNbreJoueurs, pintNbreTours;
    private String ptextMode1, ptextMode2, ptextMode3;
    List<ObjetFrappe> plistFrappe = new ArrayList<ObjetFrappe>();
    ObjetFrappe[][] ptabFrappe;

    public ObjetCompetition(int intNbreJoueurs, int intNbreTours, String textMode1, List<ObjetFrappe> listFrappe){
        pintNbreJoueurs = intNbreJoueurs;
        pintNbreTours = intNbreTours;
        ptextMode1 = textMode1;
        plistFrappe = listFrappe;
       // SetTabFrappe();
    }

    int GetNbrJoueurs(){
        return pintNbreJoueurs;
    }

    int GetNbrTours(){
        return pintNbreTours;
    }

    String GetMode1(){
        return ptextMode1;
    }

    List<ObjetFrappe> GetListFrappe(){
        return plistFrappe;
    }

    public void SetTabFrappe(ObjetFrappe[][] tabFrappe){
        ptabFrappe = tabFrappe;//new ObjetFrappe[pintNbreTours][pintNbreJoueurs];
    }

    public void majTabFrappe(int tours, int joueurs, ObjetFrappe tape){
        ptabFrappe[tours][joueurs] = tape;
    }

    public ObjetFrappe[][] GetTabFrappe() {
        return ptabFrappe;
    }

    void SetNbrJoueurs(int intNbreJoueurs){
        pintNbreJoueurs = intNbreJoueurs;
    }

    void SetNbreTours(int intNbreTours){
        pintNbreTours = intNbreTours;
    }

    void SetMode1(String textMode1){
        ptextMode1 = textMode1;
    }

    void SetListFrappe(List<ObjetFrappe> listFrappe){
        plistFrappe = listFrappe;
    }
}

