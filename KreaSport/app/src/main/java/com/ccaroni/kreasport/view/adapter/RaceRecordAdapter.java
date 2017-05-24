package com.ccaroni.kreasport.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.data.realm.RealmRace;
import com.ccaroni.kreasport.data.realm.RealmRaceRecord;

import io.realm.RealmResults;

/**
 * Created by Master on 24/05/2017.
 */

public class RaceRecordAdapter extends ArrayAdapter<RealmRaceRecord> {

    private static class ViewHolder {
        TextView id;
        TextView date;
    }

    public RaceRecordAdapter(Context context, RealmResults<RealmRaceRecord> records) {
        super(context, R.layout.layout_record_item, records);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        RealmRaceRecord record = getItem(position);


        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.layout_record_item, parent, false);

            viewHolder.id = (TextView) convertView.findViewById(R.id.tv_race_id);
            viewHolder.date = (TextView) convertView.findViewById(R.id.tv_date);

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

        // Return the completed view to render on screen
        return convertView;
    }
}
