package com.ccaroni.kreasport.view.activities.rebuild;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ccaroni.kreasport.R;

public class ExploreFragment extends Fragment {

    public ExploreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onPause() {
        super.onPause();
        Toast.makeText(this.getContext(), "frag pausew", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(this.getContext(), "frag resume", Toast.LENGTH_SHORT).show();
    }
}
