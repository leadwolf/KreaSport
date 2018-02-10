package com.ccaroni.kreasport.race.view.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.databinding.FragmentExploreBinding;

public class ExploreFragment extends Fragment {

    private static final String TAG = ExploreFragment.class.getSimpleName();

    public ExploreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentExploreBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_explore, container, false);
        View view = binding.getRoot();

        return view;
    }

}
