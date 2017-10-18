package ru.belogurowdev.lab7.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import ru.belogurowdev.lab7.R;

/**
 * Created by alexbelogurow on 18.10.2017.
 */

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    private SharedPreferences mSharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_settings);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
//        Boolean wiFiEnable = mSharedPreferences.getBoolean(getString(R.string.pref_check_key), false);


    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
}
