package com.ccaroni.kreasport.view.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.data.RealmHelper;
import com.ccaroni.kreasport.data.dto.RaceRecord;
import com.ccaroni.kreasport.data.realm.RealmRaceRecord;
import com.ccaroni.kreasport.databinding.ActivityMyRecordsBinding;
import com.ccaroni.kreasport.network.ApiUtils;
import com.ccaroni.kreasport.network.RaceRecordService;
import com.ccaroni.kreasport.utils.CredentialsManager;
import com.ccaroni.kreasport.view.adapter.RaceRecordAdapter;

import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyRecordsActivity extends AppCompatActivity implements RaceRecordAdapter.RaceRecordCommunication {

    private static final String LOG = MyRecordsActivity.class.getSimpleName();


    private ActivityMyRecordsBinding binding;

    private RaceRecordService raceRecordService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_records);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setBindings();

        String accessToken = CredentialsManager.getCredentials(this).getAccessToken();
        raceRecordService = ApiUtils.getRaceRecordService(true, accessToken);

    }

    private void setBindings() {
        final String userId = CredentialsManager.getUserId(this);

        RealmResults<RealmRaceRecord> myRecords = RealmHelper.getInstance(this).getMyRecords(userId).sort("dateTime");

        RaceRecordAdapter adapter = new RaceRecordAdapter(this, myRecords);
        binding.contentMyRecords.listViewRecords.setAdapter(adapter);
        binding.contentMyRecords.tvNbRecords.setText("" + myRecords.size());
    }

    @Override
    public void uploadRaceRecord(RaceRecord raceRecord) {
        Log.d(LOG, "call to upload race record: " + raceRecord.getId());
        raceRecordService.uploadRaceRecord(raceRecord).enqueue(new Callback<RaceRecord>() {
            @Override
            public void onResponse(Call<RaceRecord> call, Response<RaceRecord> response) {
                Toast.makeText(MyRecordsActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                Log.i(LOG, "raceRecord submitted to API." + response.body().toString());
            }

            @Override
            public void onFailure(Call<RaceRecord> call, Throwable t) {
                Log.e(LOG, "Unable to submit post to API: " + t.getMessage());
            }
        });
    }
}
