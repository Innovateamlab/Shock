package com.example.guillaume.adict_impact;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by lesgourgues on 12/06/2015.
 */
public class IntentStart extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = new Intent(IntentStart.this, MainActivity.class);
        startActivity(i);
        IntentStart.this.finish();
    }
}
