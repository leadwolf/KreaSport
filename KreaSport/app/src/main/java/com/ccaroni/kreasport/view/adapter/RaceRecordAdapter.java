package com.ccaroni.kreasport.view.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.data.dto.RaceRecord;
import com.ccaroni.kreasport.data.realm.RealmRaceRecord;

import io.realm.RealmResults;

/**
 * Created by Master on 24/05/2017.
 */

public class RaceRecordAdapter extends ArrayAdapter<RealmRaceRecord> {

    private RaceRecordCommunication raceRecordCommunication;

    private static class ViewHolder {
        TextView id;
        TextView date;
        Button buttonUpload;
    }

    public RaceRecordAdapter(Activity activity, RealmResults<RealmRaceRecord> records) {
        super(activity, R.layout.layout_record_item, records);

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
            viewHolder.buttonUpload = (Button) convertView.findViewById(R.id.btn_upload);

            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.id.setText(record.getRaceId());
        viewHolder.date.setText(record.getDateTime());
        viewHolder.buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                raceRecordCommunication.uploadRaceRecord(record.toDTO());
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }

    public interface RaceRecordCommunication {
        public void uploadRaceRecord(RaceRecord raceRecord);
    }

}
