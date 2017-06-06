package com.ccaroni.kreasport.view.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.data.RealmHelper;
import com.ccaroni.kreasport.data.dto.RaceRecord;
import com.ccaroni.kreasport.data.realm.RealmRace;
import com.ccaroni.kreasport.data.realm.RealmRaceRecord;
import com.ccaroni.kreasport.databinding.ActivityRecordBinding;
import com.ccaroni.kreasport.map.viewmodels.MapVM;
import com.ccaroni.kreasport.map.views.CustomMapView;
import com.ccaroni.kreasport.utils.Constants;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import static android.os.Build.VERSION_CODES.M;
import static com.ccaroni.kreasport.utils.Constants.KEY_RECORD_ID;

public class RecordActivity extends AppCompatActivity implements CustomMapView.MapViewCommunication {

    private ActivityRecordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_record);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setInfo();
    }

    private void setInfo() {
        final String recordId = getIntent().getStringExtra(KEY_RECORD_ID);
        if (recordId == null) {
            throw new IllegalArgumentException("Started activity with no input record");
        }

        final RealmRaceRecord raceRecord = RealmHelper.getInstance(this).findRecordById(recordId);

        binding.contentRecord.tvRecordId.setText(getString(R.string.record_id, recordId));
        binding.contentRecord.tvRaceId.setText(getString(R.string.race_id, raceRecord.getRaceId()));
        String date = LocalDate.parse(raceRecord.getDateTime(), DateTimeFormatter.ISO_OFFSET_DATE_TIME).toString();
        binding.contentRecord.tvRecordDate.setText(getString(R.string.record_date, date));

        RealmRace realmRace = RealmHelper.getInstance(this).findRaceById(raceRecord.getRaceId());
        if (realmRace != null) {

            GeoPoint center = new GeoPoint(realmRace.getLatitude(), realmRace.getLongitude());

            MapVM mMapVM = new MapVM(center, Constants.DEFAULT_ZOOM_MAP_ITEM);
            MapView mMapView = new CustomMapView(this, null, mMapVM);
            mMapView.setClickable(false);
            binding.contentRecord.rlRaceMap.addView(mMapView);
        }

        binding.contentRecord.btnDeleteRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(KEY_RECORD_ID, recordId);

                setResult(RESULT_OK, intent);

                finish();

            }
        });
    }

    @Override
    public void onMapBackgroundTouch() {

    }
}
