package com.onclavesystems.cestemoeducare;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SharedPreferences appPrefs = getActivity().getSharedPreferences("com.onclavesystems.cestemoeducare_preferences", Context.MODE_PRIVATE);
        String s1 = appPrefs.getString("registrationno", "");
        Toast.makeText(getActivity(), s1, Toast.LENGTH_SHORT).show();

        String s2 = appPrefs.getString("name", "");
        Toast.makeText(getActivity(), s2, Toast.LENGTH_SHORT).show();

        SharedPreferences sp = getActivity().getSharedPreferences("hiddenpref", Context.MODE_PRIVATE);
        Boolean s3 = sp.getBoolean("registered", false);
        Toast.makeText(getActivity(), s3.toString(), Toast.LENGTH_SHORT).show();
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}
