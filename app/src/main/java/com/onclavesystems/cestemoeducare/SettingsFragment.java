package com.onclavesystems.cestemoeducare;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.design.widget.Snackbar;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment {
    private volatile String registrationNumber = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_app_preference);
        final EditTextPreference pref = (EditTextPreference)findPreference("registrationno");

        pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferences sp = getActivity().getSharedPreferences("com.onclavesystems.cestemoeducare_preferences", Context.MODE_PRIVATE);
                registrationNumber = sp.getString("registrationno", "");
                EditText et = ((EditTextPreference) findPreference("registrationno")).getEditText();

                if (registrationNumber.equals("")) {
                    et.setText("");
                } else {
                    et.setText(registrationNumber);
                }

                return false;
            }
        });

        pref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (!newValue.equals("")) {
                    SharedPreferences appPrefs = getActivity().getSharedPreferences("com.onclavesystems.cestemoeducare_preferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor prefEd = appPrefs.edit();
                    prefEd.putString("name", "Debabrata");
                    prefEd.putString("registrationno", pref.getText());
                    prefEd.apply();

                    SharedPreferences sp = getActivity().getSharedPreferences("hiddenpref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("registered", true);
                    editor.apply();

                    if (getView() != null) {
                        Snackbar.make(getView(), "Your preferences were saved", Snackbar.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity().getBaseContext(), "Your preferences were Saved", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    return false;
                }

                return true;
            }
        });

        Preference button = (Preference) findPreference("forgetRegistrationNumber");
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Forget Associated Account")
                        .setMessage("Are you seure you want to forget Associated Account?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences appPrefs = getActivity().getSharedPreferences("com.onclavesystems.cestemoeducare_preferences", Context.MODE_PRIVATE);
                                SharedPreferences.Editor prefEd = appPrefs.edit();
                                prefEd.putString("registrationno", "");
                                prefEd.putString("name", "");
                                prefEd.apply();

                                SharedPreferences sp = getActivity().getSharedPreferences("hiddenpref", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putBoolean("registered", false);
                                editor.apply();

                                registrationNumber = "";
                            }
                        }).create().show();

                return true;
            }
        });
    }
}
