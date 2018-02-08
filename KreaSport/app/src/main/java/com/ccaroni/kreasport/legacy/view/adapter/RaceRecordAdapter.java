package com.ccaroni.kreasport.legacy.view.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.legacy.data.RealmHelper;
import com.ccaroni.kreasport.legacy.data.realm.RealmRace;
import com.ccaroni.kreasport.legacy.data.realm.RealmRaceRecord;
import com.ccaroni.kreasport.map.MapDefaults;
import com.ccaroni.kreasport.map.views.CustomMapView;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.List;

import static com.ccaroni.kreasport.utils.Constants.DEFAULT_ZOOM_MAP_ITEM;

/**
 * Created by Master on 24/05/2017.
 */

public class RaceRecordAdapter extends ArrayAdapter<RealmRaceRecord> {

    private static final String LOG = RaceRecordAdapter.class.getSimpleName();


    private Activity activity;
    private RaceRecordCommunication raceRecordCommunication;

    private static class ViewHolder {
        TextView raceId;
        TextView recordId;
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

            viewHolder.raceId = (TextView) convertView.findViewById(R.id.tv_race_id);
            viewHolder.recordId = (TextView) convertView.findViewById(R.id.tv_record_id);
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
        viewHolder.raceId.setText(getContext().getString(R.string.race_id, record.getRaceId()));
        viewHolder.recordId.setText(getContext().getString(R.string.record_id, record.getId()));
        String date = LocalDate.parse(record.getDateTime(), DateTimeFormatter.ISO_OFFSET_DATE_TIME).toString();
        viewHolder.date.setText(getContext().getString(R.string.record_date, date));


        RealmRace realmRace = RealmHelper.getInstance(activity).findRaceById(record.getRaceId());
        if (realmRace != null) {
            GeoPoint center = new GeoPoint(realmRace.getLatitude(), realmRace.getLongitude());

            MapDefaults mMapDefaults = new MapDefaults(center, DEFAULT_ZOOM_MAP_ITEM);
                MapView mMapView = new CustomMapView(activity, null, null, mMapDefaults);

            mMapView.setMaxZoomLevel(DEFAULT_ZOOM_MAP_ITEM);
            mMapView.setMinZoomLevel(DEFAULT_ZOOM_MAP_ITEM);
            mMapView.setScrollableAreaLimitDouble(realmRace.getBoundingBox());

            mMapView.setOnClickListener(onItemClick(raceRecordCommunication, record));
//            mMapView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                        raceRecordCommunication.onRecordSelection(record);
//                    }
//                    return true;
//                }
//            });
            viewHolder.rlMap.addView(mMapView);
        }

        convertView.setOnClickListener(onItemClick(raceRecordCommunication, record));

        // Return the completed view to render on screen
        return convertView;
    }

    public interface RaceRecordCommunication {
        void onRecordSelection(RealmRaceRecord realmRaceRecord);
    }

    private static View.OnClickListener onItemClick(final RaceRecordCommunication raceRecordCommunication, final RealmRaceRecord record) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                raceRecordCommunication.onRecordSelection(record);
            }
        };
    }

}
