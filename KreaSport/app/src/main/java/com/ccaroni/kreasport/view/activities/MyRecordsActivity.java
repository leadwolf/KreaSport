package com.ccaroni.kreasport.view.activities;

import android.content.Intent;
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
import com.ccaroni.kreasport.map.views.CustomMapView;
import com.ccaroni.kreasport.network.ApiUtils;
import com.ccaroni.kreasport.network.RaceRecordService;
import com.ccaroni.kreasport.utils.Constants;
import com.ccaroni.kreasport.utils.CredentialsManager;
import com.ccaroni.kreasport.view.adapter.RaceRecordAdapter;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ccaroni.kreasport.utils.Constants.KEY_RECORD_ID;

public class MyRecordsActivity extends AppCompatActivity implements RaceRecordAdapter.RaceRecordCommunication, CustomMapView.MapViewCommunication {

    private static final String LOG = MyRecordsActivity.class.getSimpleName();
    public static final int REQUEST_CODE_RECORD_ID_TO_DELETE = 100;


    private ActivityMyRecordsBinding binding;

    private RaceRecordService raceRecordService;
    private RaceRecordAdapter raceRecordAdapter;

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

        List<RealmRaceRecord> realmRaceRecordList = new ArrayList<>();
        for (RealmRaceRecord realmRaceRecord : myRecords) {
            realmRaceRecordList.add(realmRaceRecord);
        }

        myRecords = null;

        raceRecordAdapter = new RaceRecordAdapter(this, realmRaceRecordList);
        binding.contentMyRecords.listViewRecords.setAdapter(raceRecordAdapter);

        setNumberOfRecords(raceRecordAdapter.getCount());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_RECORD_ID_TO_DELETE) {
            if (resultCode == RESULT_OK) {
                String recordIdToDelete = data.getStringExtra(KEY_RECORD_ID);
                if (recordIdToDelete == null) {
                    throw new IllegalArgumentException("Called to delete record but no id found");
                }

                RealmRaceRecord raceRecord = RealmHelper.getInstance(this).findRecordById(recordIdToDelete);
                raceRecordAdapter.remove(raceRecord);
                raceRecordAdapter.notifyDataSetChanged();

                deleteRemoteRaceRecord(raceRecord.getId());


                RealmHelper.getInstance(this).beginTransaction();
                raceRecord.deleteFromRealm();
                RealmHelper.getInstance(this).commitTransaction();


                setNumberOfRecords(raceRecordAdapter.getCount());
            }
        }
    }


    @Override
    public void onMapBackgroundTouch() {
    }

    private void setNumberOfRecords(int nb) {
        binding.contentMyRecords.tvNbRecords.setText(getString(R.string.number_records_title, nb));
    }

    private void deleteRemoteRaceRecord(String recordId) {
        Log.d(LOG, "call to delete race record " + recordId);
        raceRecordService.deleteRaceRecord(recordId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d(LOG, "delete success: " + response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(LOG, "Unable to delete race record from server: " + t.getMessage());
            }
        });
    }

    @Override
    public void onRecordSelection(RealmRaceRecord realmRaceRecord) {
        Intent intent = new Intent(this, RecordActivity.class);
        intent.putExtra(Constants.KEY_RECORD_ID, realmRaceRecord.getId());
        startActivityForResult(intent, MyRecordsActivity.REQUEST_CODE_RECORD_ID_TO_DELETE); // if a race is deleted, ot will use this request code
    }
}
