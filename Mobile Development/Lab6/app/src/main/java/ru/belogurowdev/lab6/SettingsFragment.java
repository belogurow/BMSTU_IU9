package ru.belogurowdev.lab6;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by alexbelogurow on 11.10.2017.
 */

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    private SharedPreferences mSharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_settings);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
        Boolean wiFiEnable = mSharedPreferences.getBoolean(getString(R.string.pref_check_key), false);


    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Preference preference = findPreference(s);

        if (preference != null) {


            if (preference instanceof EditTextPreference) {
                String string = mSharedPreferences.getString(s, "123");
                preference.setSummary(string);
                //String value = sharedPreferences.getString(getString(R.string.pref_check_key), "");
                //setPreferenceS(preference, value);
            }

            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                String string = mSharedPreferences.getString(preference.getKey(), "");
                int index = listPreference.findIndexOfValue(string);

                if (index >= 0) {
                    preference.setSummary(listPreference.getEntries()[index]);
                }
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
}
