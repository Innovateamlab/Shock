package com.example.guillaume.adict_impact.modele;

/**
 * Created by ludovic on 28/02/17.
 */

public class ObjetNotification
{
    public String label;
    public Object object;

    public ObjetNotification(String label, Object obj)
    {
        this.label = label;
        this.object = obj;
    }

    public ObjetFrappe getObjetFrappe() {return (ObjetFrappe) object;}
    public String getString(){return (String) object;}
}
