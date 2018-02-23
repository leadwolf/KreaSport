package com.ccaroni.kreasport.explore.view.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ccaroni.kreasport.R;

public class OfflineAreasFragment extends Fragment {

    public OfflineAreasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_offline_areas, container, false);
    }

}
