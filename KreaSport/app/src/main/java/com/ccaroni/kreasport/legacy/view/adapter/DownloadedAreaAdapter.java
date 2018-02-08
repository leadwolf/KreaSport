package com.ccaroni.kreasport.legacy.view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ccaroni.kreasport.R;
import com.ccaroni.kreasport.legacy.data.realm.DownloadedArea;
import com.ccaroni.kreasport.legacy.view.activities.downloads.DownloadedAreaActivity;
import com.ccaroni.kreasport.legacy.view.activities.menu.OfflineAreasActivity;

import java.util.List;

import static com.ccaroni.kreasport.utils.Constants.KEY_AREA_ID;

/**
 * Created by Master on 02/06/2017.
 */

public class DownloadedAreaAdapter extends ArrayAdapter<DownloadedArea> {

    private static final String LOG = DownloadedAreaAdapter.class.getSimpleName();
    private Activity originActivity;

    private static class ViewHolder {
        ImageView image;
        TextView name;
        TextView size;
        ProgressBar progressBar;
    }

    public DownloadedAreaAdapter(Activity originActivity, List<DownloadedArea> downloadedAreas) {
        super(originActivity, R.layout.layout_downloaded_area, downloadedAreas);
        this.originActivity = originActivity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final DownloadedArea downloadedArea = getItem(position);

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

        Drawable drawable = downloadedArea.isOngoing() ? ContextCompat.getDrawable(getContext(), R.drawable.ic_file_download_black_24dp) : ContextCompat.getDrawable(getContext()
                , R.drawable.ic_check_circle_black_24dp);
        viewHolder.image.setImageDrawable(drawable);
        viewHolder.name.setText(downloadedArea.getName());
        viewHolder.size.setText(Formatter.formatShortFileSize(originActivity, (long) downloadedArea.getSize()));
        viewHolder.progressBar.setMax(100);
        int roundedProgress = (int) Math.round(downloadedArea.getProgress());
        viewHolder.progressBar.setProgress(roundedProgress);

        viewHolder.size.setVisibility(downloadedArea.isOngoing() ? View.GONE : View.VISIBLE);
        viewHolder.progressBar.setVisibility(downloadedArea.isOngoing() ? View.VISIBLE : View.GONE);

        viewHolder.progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.primary), PorterDuff.Mode.MULTIPLY);
        viewHolder.progressBar.getProgressDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.primary), PorterDuff.Mode.MULTIPLY);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DownloadedAreaActivity.class);
                intent.putExtra(KEY_AREA_ID, downloadedArea.getId());
                originActivity.startActivityForResult(intent, OfflineAreasActivity.REQUEST_CODE_DELETION);
            }
        });

        // Return the completed view to render on screen
        return convertView;

    }



}
