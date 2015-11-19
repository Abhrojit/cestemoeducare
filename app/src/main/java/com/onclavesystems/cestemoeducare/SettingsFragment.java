package com.onclavesystems.cestemoeducare;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment {
    private volatile String registrationNumber = "", emailId = "";
    private boolean netCheck = false;
    private String validResponse="";
    private static String login_url = "";
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
                    if (isInternetOn()) {
                        netCheck = true;
                        login_url = getString(R.string.url_login) + pref.getText();
                        checkData();
                        SharedPreferences sp = getActivity().getSharedPreferences("com.onclavesystems.cestemoeducare_preferences", Context.MODE_PRIVATE);

                        if (getView() != null) {
                            Snackbar.make(getView(), sp.getString("name", "") + ",your preferences were saved", Snackbar.LENGTH_SHORT).show();
                        }
                    } else {
                        Snackbar.make(getView(), "Error connecting to net", Snackbar.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    return false;
                }

                return true;
            }
        });

        final EditTextPreference prefemail = (EditTextPreference)findPreference("emailid");

        prefemail.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferences sp = getActivity().getSharedPreferences("com.onclavesystems.cestemoeducare_preferences", Context.MODE_PRIVATE);
                emailId = sp.getString("emailid", "");
                EditText et = ((EditTextPreference) findPreference("emailid")).getEditText();

                if (emailId.equals("")) {
                    et.setText("");
                } else {
                    et.setText(emailId);
                }

                return false;
            }
        });

        prefemail.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if (!newValue.equals("")) {

                    if (getView() != null) {
                        if(checkvalidemail()) {
                            SharedPreferences appPrefs = getActivity().getSharedPreferences("com.onclavesystems.cestemoeducare_preferences", Context.MODE_PRIVATE);
                            SharedPreferences.Editor prefEd = appPrefs.edit();
                            prefEd.putString("emailid", String.valueOf(prefemail));

                            SharedPreferences sp = getActivity().getSharedPreferences("hiddenpref", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putBoolean("registeredemail", true);
                            editor.apply();
                            Snackbar.make(getView(), "your emailId is saved", Snackbar.LENGTH_SHORT).show();
                        }
                        else{
                            prefemail.setText("");
                            Snackbar.make(getView(), "your emailId is invalid", Snackbar.LENGTH_SHORT).show();
                        }
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
                        .setMessage("Are you sure you want to forget Associated Account?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences appPrefs = getActivity().getSharedPreferences("com.onclavesystems.cestemoeducare_preferences", Context.MODE_PRIVATE);
                                SharedPreferences.Editor prefEd = appPrefs.edit();
                                prefEd.putString("registrationno", "");
                                prefEd.putString("name", "");
                                prefEd.putString("emailid", "");
                                prefEd.apply();

                                SharedPreferences sp = getActivity().getSharedPreferences("hiddenpref", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putBoolean("registered", false);
                                editor.putBoolean("registeredemail", false);
                                editor.apply();

                                registrationNumber = "";
                            }
                        }).create().show();

                return true;
            }
        });
    }


    private boolean isInternetOn() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;

        }
    }

    private void checkData(){
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, login_url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    validResponse = response.getString("response");
                }
                catch(JSONException e) {
                    Snackbar.make(getView(), "Error in response", Snackbar.LENGTH_SHORT).show();
                }
                try{
                    String name = response.getString("name");
                    boolean success = response.getBoolean("success");
                    if(success){
                        setData(name);
                    }else{
                        Snackbar.make(getView(), validResponse, Snackbar.LENGTH_SHORT).show();
                    }
                }catch(JSONException e){
                    Snackbar.make(getView(), validResponse, Snackbar.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Snackbar.make(getView(), "Response Error", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void setData(String name){
        final EditTextPreference pref = (EditTextPreference)findPreference("registrationno");
        SharedPreferences appPrefs = getActivity().getSharedPreferences("com.onclavesystems.cestemoeducare_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEd = appPrefs.edit();
        prefEd.putString("name", name);
        prefEd.putString("registrationno", pref.getText());
        prefEd.apply();

        SharedPreferences sp = getActivity().getSharedPreferences("hiddenpref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("registered", true);
        editor.apply();
    }

    public boolean checkvalidemail(){
        final EditTextPreference prefemail = (EditTextPreference)findPreference("emailid");
        String expression = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher((prefemail).getText().toString().trim());

        if(!matcher.matches()) {
            return false;
        }
        else {
            return true;
        }
    }
}
