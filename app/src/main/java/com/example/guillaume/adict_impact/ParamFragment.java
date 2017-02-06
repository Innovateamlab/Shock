package com.example.guillaume.adict_impact;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class ParamFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings);
    }

}