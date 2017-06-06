package com.ccaroni.kreasport.view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.data.RealmHelper;
import com.ccaroni.kreasport.data.dto.RaceRecord;
import com.ccaroni.kreasport.data.realm.RealmRace;
import com.ccaroni.kreasport.data.realm.RealmRaceRecord;
import com.ccaroni.kreasport.map.viewmodels.MapVM;
import com.ccaroni.kreasport.map.views.CustomMapView;
import com.ccaroni.kreasport.utils.Constants;
import com.ccaroni.kreasport.view.activities.MyRecordsActivity;
import com.ccaroni.kreasport.view.activities.RecordActivity;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.List;

import io.realm.RealmResults;

/**
 * Created by Master on 24/05/2017.
 */

public class RaceRecordAdapter extends ArrayAdapter<RealmRaceRecord> {

    private static final String LOG = RaceRecordAdapter.class.getSimpleName();


    private Activity activity;
    private RaceRecordCommunication raceRecordCommunication;

    private static class ViewHolder {
        TextView id;
        TextView date;
        RelativeLayout rlMap;
    }

    public RaceRecordAdapter(Activity activity, List<RealmRaceRecord> records) {
        super(activity, R.layout.layout_record_item, records);

        this.activity = activity;

        if (activity instanceof RaceRecordCommunication) {
            this.raceRecordCommunication = (RaceRecordCommunication) activity;
        } else {
            throw new RuntimeException(activity + " must implement " + RaceRecordCommunication.class.getSimpleName());
        }

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final RealmRaceRecord record = getItem(position);


        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.layout_record_item, parent, false);

            viewHolder.id = (TextView) convertView.findViewById(R.id.tv_race_id);
            viewHolder.date = (TextView) convertView.findViewById(R.id.tv_date);
            viewHolder.rlMap = (RelativeLayout) convertView.findViewById(R.id.rl_race_map);

            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.id.setText(getContext().getString(R.string.race_id, record.getRaceId()));
        String date = LocalDate.parse(record.getDateTime(), DateTimeFormatter.ISO_OFFSET_DATE_TIME).toString();
        viewHolder.date.setText(getContext().getString(R.string.record_date, date));


        RealmRace realmRace = RealmHelper.getInstance(activity).findRaceById(record.getRaceId());
        if (realmRace != null) {
            GeoPoint center = new GeoPoint(realmRace.getLatitude(), realmRace.getLongitude());

            MapVM mMapVM = new MapVM(center, Constants.DEFAULT_ZOOM_MAP_ITEM);
            MapView mMapView = new CustomMapView(activity, null, mMapVM);
            mMapView.setClickable(false);
            viewHolder.rlMap.addView(mMapView);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, RecordActivity.class);
                intent.putExtra(Constants.KEY_RECORD_ID, record.getId());
                activity.startActivityForResult(intent, MyRecordsActivity.REQUEST_CODE_RECORD_ID_TO_DELETE);
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }

    public interface RaceRecordCommunication {

    }

}
