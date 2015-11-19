package com.onclavesystems.cestemoeducare;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LocationFragment extends Fragment {
    String[] centerNames;

    String[] addresses;
    private static String login_url = "";
    private String validResponse = "";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isInternetOn()){
            getCentres();
        }else{
            Snackbar.make(getView(), "Error connecting to net", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RecyclerView locationRecycler = (RecyclerView) inflater.inflate(R.layout.fragment_location, container, false);
        LocationListAdapter adapter = new LocationListAdapter(centerNames, addresses);
        locationRecycler.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        locationRecycler.setLayoutManager(layoutManager);

        return locationRecycler;
    }

    private boolean isInternetOn() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;

        }
    }

    public void getCentres(){
        login_url = getString(R.string.url_login);
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
                    response = response.getJSONObject("Centres");
                    JSONArray jArray = response.getJSONArray("Centres");
                    centerNames = new String[jArray.length()];
                    addresses = new String[jArray.length()];
                    for(int i=0; i<jArray.length(); i++) {
                        JSONObject json_data = jArray.getJSONObject(i);

                        centerNames[i] = json_data.getString("centre");
                        addresses[i] = json_data.getString("address");
                    }

                    boolean success = response.getBoolean("success");
                    if(!success){
                        Snackbar.make(getView(), "Couldnt retrieve data", Snackbar.LENGTH_SHORT).show();
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

}
