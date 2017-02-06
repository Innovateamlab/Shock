package com.example.guillaume.adict_impact;


import android.app.Activity;
import android.os.Bundle;

public class ParamActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new ParamFragment()).commit();
    }

}

