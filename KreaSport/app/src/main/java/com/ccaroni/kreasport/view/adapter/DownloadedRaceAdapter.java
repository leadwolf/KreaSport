package com.ccaroni.kreasport.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.data.realm.RealmRace;

import io.realm.RealmResults;

/**
 * Created by Master on 22/05/2017.
 */

public class DownloadedRaceAdapter extends ArrayAdapter<RealmRace> {

    private static final String LOG = DownloadedRaceAdapter.class.getSimpleName();

    public DownloadedRaceAdapter(Context context, RealmResults<RealmRace> races) {
        super(context, 0, races);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        RealmRace race = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_race_item, parent, false);
        }

        // Lookup view for race
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_downloaded_race_name);
        TextView tvID = (TextView) convertView.findViewById(R.id.tv_downloaded_race_id);
        TextView tvNbCheckpoints = (TextView) convertView.findViewById(R.id.tv_downloaded_race_nb_checkpoints);

        tvName.setText(race.getTitle());
        tvID.setText(race.getId());

        tvNbCheckpoints.setText("" + race.getRealmCheckpoints().size());

        return convertView;
    }
}
