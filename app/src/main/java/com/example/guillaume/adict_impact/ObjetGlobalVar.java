package com.example.guillaume.adict_impact;

import java.io.Serializable;

/**
 * Created by lesgourgues on 12/06/2015.
 */
public class ObjetGlobalVar implements Serializable {

    boolean pboulConnected;
   public ObjetGlobalVar(boolean boulConnected){
       pboulConnected = boulConnected;
   }


    void setBoulConnected(boolean boulConnected){
        pboulConnected = boulConnected;
    }

    boolean getBoulConnected(){

        return pboulConnected;
    }
}
