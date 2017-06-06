package com.ccaroni.kreasport.view.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.data.RealmHelper;
import com.ccaroni.kreasport.data.dto.RaceRecord;
import com.ccaroni.kreasport.data.realm.RealmRace;
import com.ccaroni.kreasport.data.realm.RealmRaceRecord;
import com.ccaroni.kreasport.databinding.ActivityRecordBinding;
import com.ccaroni.kreasport.map.viewmodels.MapVM;
import com.ccaroni.kreasport.map.views.CustomMapView;
import com.ccaroni.kreasport.network.ApiUtils;
import com.ccaroni.kreasport.network.RaceRecordService;
import com.ccaroni.kreasport.utils.Constants;
import com.ccaroni.kreasport.utils.CredentialsManager;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.os.Build.VERSION_CODES.M;
import static com.ccaroni.kreasport.utils.Constants.KEY_RECORD_ID;

public class RecordActivity extends AppCompatActivity implements CustomMapView.MapViewCommunication {

    private static final String LOG = RecordActivity.class.getSimpleName();


    private ActivityRecordBinding binding;
    private RaceRecordService raceRecordService;
    private RealmRaceRecord raceRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_record);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String accessToken = CredentialsManager.getCredentials(this).getAccessToken();
        raceRecordService = ApiUtils.getRaceRecordService(true, accessToken);

        setInfo();
    }

    private void setInfo() {
        final String recordId = getIntent().getStringExtra(KEY_RECORD_ID);
        if (recordId == null) {
            throw new IllegalArgumentException("Started activity with no input record");
        }

        raceRecord = RealmHelper.getInstance(this).findRecordById(recordId);

        binding.contentRecord.tvRecordId.setText(getString(R.string.record_id, recordId));
        binding.contentRecord.tvRaceId.setText(getString(R.string.race_id, raceRecord.getRaceId()));
        String date = LocalDate.parse(raceRecord.getDateTime(), DateTimeFormatter.ISO_OFFSET_DATE_TIME).toString();

        RealmRace realmRace = RealmHelper.getInstance(this).findRaceById(raceRecord.getRaceId());
        if (realmRace != null) {

            GeoPoint center = new GeoPoint(realmRace.getLatitude(), realmRace.getLongitude());

            MapVM mMapVM = new MapVM(center, Constants.DEFAULT_ZOOM_MAP_ITEM);
            MapView mMapView = new CustomMapView(this, null, mMapVM);
            mMapView.setClickable(false);
            binding.contentRecord.rlRaceMap.addView(mMapView);
        }

        /* DETAILS */

        binding.contentRecord.tvRecordDate.setText(getString(R.string.record_date, date));

        updateUploadStatus();

        /* END DETAILS */

        binding.contentRecord.btnDeleteRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(KEY_RECORD_ID, recordId);

                setResult(RESULT_OK, intent);

                finish();

            }
        });

        binding.contentRecord.btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                raceRecordService.uploadRaceRecord(raceRecord.toDTO()).enqueue(new Callback<RaceRecord>() {
                    @Override
                    public void onResponse(Call<RaceRecord> call, Response<RaceRecord> response) {
                        Log.d(LOG, "on response for race upload");
                    }

                    @Override
                    public void onFailure(Call<RaceRecord> call, Throwable t) {
                        Log.d(LOG, "error uploading race record: " + t.getMessage());
                    }
                });
            }
        });
    }

    private void updateUploadStatus() {
        raceRecordService.getRaceRecord(raceRecord.getId()).enqueue(new Callback<RaceRecord>() {
            @Override
            public void onResponse(Call<RaceRecord> call, Response<RaceRecord> response) {
                if (response.isSuccessful()) {
                    Log.d(LOG, "correctly found race record, means is already uploaded");
                    setUploadStatus(true);
                } else {
                    Log.d(LOG, "error getting response to see if race record uploaded");
                    setUploadStatus(false);
                }
            }

            @Override
            public void onFailure(Call<RaceRecord> call, Throwable t) {
                Log.d(LOG, "error calling to check if race record uploaded: " + t.getMessage());
                setUploadStatus(false);
            }
        });
    }

    @Override
    public void onMapBackgroundTouch() {

    }

    private void setUploadStatus(boolean isUploaded) {
        if (isUploaded) {
            binding.contentRecord.imageViewUploadStatus.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_cloud_done_black_24dp));
        } else {
            binding.contentRecord.imageViewUploadStatus.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_cloud_upload_black_24dp));
        }
    }
}
