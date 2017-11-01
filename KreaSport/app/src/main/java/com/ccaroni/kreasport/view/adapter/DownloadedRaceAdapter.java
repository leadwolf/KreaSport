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
import com.ccaroni.kreasport.data.legacy.realm.RealmRace;

import io.realm.RealmResults;

/**
 * Created by Master on 22/05/2017.
 */

public class DownloadedRaceAdapter extends ArrayAdapter<RealmRace> {

    private static final String LOG = DownloadedRaceAdapter.class.getSimpleName();

    private static class ViewHolder {
        TextView name;
        TextView id;
        TextView nCheckpoints;
    }

    public DownloadedRaceAdapter(Context context, RealmResults<RealmRace> races) {
        super(context, R.layout.layout_race_item, races);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        RealmRace race = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.layout_race_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_downloaded_race_name);
            viewHolder.id = (TextView) convertView.findViewById(R.id.tv_downloaded_race_id);
            viewHolder.nCheckpoints = (TextView) convertView.findViewById(R.id.tv_downloaded_race_nb_checkpoints);

            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.name.setText(race.getTitle());
        viewHolder.id.setText(race.getId());
        viewHolder.nCheckpoints.setText("" + race.getRealmCheckpoints().size());

        // Return the completed view to render on screen
        return convertView;

    }
}
