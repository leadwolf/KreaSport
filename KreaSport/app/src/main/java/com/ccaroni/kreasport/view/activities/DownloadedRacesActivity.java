package com.ccaroni.kreasport.view.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.data.RealmHelper;
import com.ccaroni.kreasport.data.legacy.realm.RealmRace;
import com.ccaroni.kreasport.databinding.ActivityDownloadedRacesBinding;
import com.ccaroni.kreasport.view.adapter.DownloadedRaceAdapter;

import io.realm.RealmResults;

public class DownloadedRacesActivity extends AppCompatActivity {

    private ActivityDownloadedRacesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_downloaded_races);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setBindings();
    }

    private void setBindings() {
        RealmResults<RealmRace> allRaces = RealmHelper.getInstance(this).getAllRaces(false);

        DownloadedRaceAdapter adapter = new DownloadedRaceAdapter(this, allRaces);

        binding.contentDownloadedRaces.listViewPublicRaces.setAdapter(adapter);

        binding.contentDownloadedRaces.tvNbPublicRaces.setText("" + allRaces.size());
    }

}
