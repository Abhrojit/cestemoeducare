package com.onclavesystems.cestemoeducare;


import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.activity_app_preference);
        final EditTextPreference pref = (EditTextPreference)findPreference("registrationno");

        pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferences sp = getActivity().getSharedPreferences("hiddenpref", Context.MODE_PRIVATE);
                Boolean registered = sp.getBoolean("registered", false);
                if (registered == false) {
                    pref.setText("");
                }

                return true;
            }
        });

        pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (!newValue.equals("")) {
                    SharedPreferences appPrefs = getActivity().getSharedPreferences("com.onclavesystems.cestemoeducare_preferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor prefEd = appPrefs.edit();
                    prefEd.putString("name", "Abhrojit");
                    prefEd.commit();

                    SharedPreferences sp = getActivity().getSharedPreferences("hiddenpref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("registered", true);
                    editor.commit();

                    Toast.makeText(getActivity(), "Your registration number is saved", Toast.LENGTH_LONG).show();
                } else {
                    return false;
                }
                return true;
            }
        });


        Preference button = (Preference) findPreference("forgetRegistrationNumber");
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference arg0) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Forget Registration Number?")
                        .setMessage("Are you sure you want us to forget your current Registration Number?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                SharedPreferences appPrefs = getActivity().getSharedPreferences("com.onclavesystems.cestemoeducare_preferences", Context.MODE_PRIVATE);
                                SharedPreferences.Editor prefEd = appPrefs.edit();
                                prefEd.putString("registrationno", "");
                                prefEd.putString("name", "");
                                prefEd.commit();

                                SharedPreferences sp = getActivity().getSharedPreferences("hiddenpref", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putBoolean("registered", false);
                                editor.commit();

                                Toast.makeText(getActivity(), "Registration number removed successfully", Toast.LENGTH_LONG).show();


                            }
                        }).create().show();
                return true;
            }
        });


    }


}
