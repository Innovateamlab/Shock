package com.example.guillaume.adict_impact.modele;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.example.guillaume.adict_impact.communication.FctBluetooth;

import java.util.Observable;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by ludovic on 27/02/17.
 */

public class SacDeFrappe extends Observable
{
    private String msg_received, tmp_received;
    private BluetoothAdapter blueAdapter;
    private FctBluetooth bt;
    private ObjetFrappe objetFrappe;

    final Handler handler = new Handler()
    {
        public void handleMessage(Message msg) {
            String data = msg.getData().getString("receivedData"); // On reçoit les "data" données envoyées par bluetooth
            String[] dataTemp = data.split("\n");

            tmp_received += dataTemp[0];

            if (tmp_received.contains("\r")) {
                dataTemp = tmp_received.split("\r");
                msg_received = dataTemp[0];

                tmp_received = "";

                Log.i("SacDeFrappe",msg_received);
                objetFrappe.ajouterEchantillonParser(msg_received);

                setChanged();
                notifyObservers(new ObjetNotification(Labels.OBJET_FRAPPE,objetFrappe));

                //TODO Ajouter la reinitilisation d'une frappe après reception de 'END' ou 'NEW' ? (MAJ de Arduino)

                objetFrappe.calculerInfos();
                objetFrappe.reset();
            }
        }
    };

    final Handler handlerStatus = new Handler() {
        public void handleMessage(Message msg) {
            int co = msg.arg1;
            if(co == 1) {
                //affichageresultat.append("Connected\n");
            } else if(co == 2) {
                //affichageresultat.append("Disconnected\n");
            }
        }
    };

    public SacDeFrappe(Activity parent) {

        objetFrappe = new ObjetFrappe();
        tmp_received = "";
        blueAdapter = BluetoothAdapter.getDefaultAdapter();
        // Demande d'activation de la fonction Bluetooth (si inactive)
        if (!blueAdapter.isEnabled()) { // i.e. "si le bluetooth est inactif..."
            Intent enableBlueTooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            parent.startActivityForResult(enableBlueTooth, parent.RESULT_OK);
        } else { // i.e. "si le bluetooth est actif..."
            Toast.makeText(parent, "Bluetooth actif", LENGTH_SHORT).show();
        }

        // Création de l'objet fonction Bluetooth
        bt = new FctBluetooth(handlerStatus, handler);

        Log.i("FctBluetooth","dodo 10s");

        bt.connect();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        sendData("A");
    }

    public void sendData(String s)
    {
        bt.sendData(s);
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
