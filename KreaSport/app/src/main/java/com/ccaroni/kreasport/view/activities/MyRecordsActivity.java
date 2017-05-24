package com.ccaroni.kreasport.view.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.data.RealmHelper;
import com.ccaroni.kreasport.data.realm.RealmRaceRecord;
import com.ccaroni.kreasport.databinding.ActivityMyRecordsBinding;
import com.ccaroni.kreasport.utils.CredentialsManager;
import com.ccaroni.kreasport.view.adapter.RaceRecordAdapter;

import io.realm.RealmResults;

public class MyRecordsActivity extends AppCompatActivity {

    private ActivityMyRecordsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_records);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setBindings();

    }

    private void setBindings() {
        final String userId = CredentialsManager.getUserId(this);

        RealmResults<RealmRaceRecord> myRecords = RealmHelper.getInstance(this).getMyRecords(userId).sort("dateTime");

        RaceRecordAdapter adapter = new RaceRecordAdapter(this, myRecords);
        binding.contentMyRecords.listViewRecords.setAdapter(adapter);
        binding.contentMyRecords.tvNbRecords.setText("" + myRecords.size());
    }

}
