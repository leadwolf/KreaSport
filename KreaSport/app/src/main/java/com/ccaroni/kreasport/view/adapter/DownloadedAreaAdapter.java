package com.ccaroni.kreasport.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.data.realm.DownloadedArea;
import com.ccaroni.kreasport.data.realm.RealmRace;

import io.realm.RealmResults;

/**
 * Created by Master on 02/06/2017.
 */

public class DownloadedAreaAdapter extends ArrayAdapter<DownloadedArea> {

    private static class ViewHolder {
        ImageView image;
        TextView name;
        TextView size;
        ProgressBar progressBar;
    }

    public DownloadedAreaAdapter(Context context, RealmResults<DownloadedArea> downloadedAreas) {
        super(context, R.layout.layout_downloaded_area, downloadedAreas);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DownloadedArea downloadedArea = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.layout_downloaded_area, parent, false);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.imageview_area);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tv_area_name);
            viewHolder.size = (TextView) convertView.findViewById(R.id.tv_area_size);
            viewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.progress_bar_downloading_area);

            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.image.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_map_black_24dp));
        viewHolder.name.setText(downloadedArea.getName());
        viewHolder.size.setText("" + Math.round(downloadedArea.getSize()) + " MB");
        viewHolder.progressBar.setMax(100);
        int roundedProgress = (int) Math.round(downloadedArea.getProgress());
        viewHolder.progressBar.setProgress(roundedProgress);

        viewHolder.size.setVisibility(downloadedArea.isOngoing() ? View.GONE : View.VISIBLE);
        viewHolder.progressBar.setVisibility(downloadedArea.isOngoing() ? View.VISIBLE : View.GONE);

        // Return the completed view to render on screen
        return convertView;

    }

}
