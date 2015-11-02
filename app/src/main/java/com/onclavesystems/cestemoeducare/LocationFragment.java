package com.onclavesystems.cestemoeducare;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LocationFragment extends Fragment {
    String[] centerNames = {
            "Center One",
            "Center Two",
            "Center Three",
            "Center Four"
    };

    String[] addresses = {
            "33/8/C, Rani Park",
            "32/7/B, Belghoria",
            "31/6/A, Kolkata",
            "30/5/Z, Pin - 700056"
    };

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
}
