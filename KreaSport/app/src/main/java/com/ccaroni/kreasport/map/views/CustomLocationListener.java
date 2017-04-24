package com.ccaroni.kreasport.map.views;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.widget.Toast;

import com.ccaroni.kreasport.R;
import com.google.android.gms.location.LocationListener;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.DirectedLocationOverlay;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Master on 24/04/2017.
 */

public class CustomLocationListener implements LocationListener {

    private final Activity activity;
    private final MapView mMapView;
    private final DirectedLocationOverlay overlay;
    private boolean hasFix = false;

    public CustomLocationListener(Activity activity, MapView mMapView, DirectedLocationOverlay overlay) {
        this.activity = activity;
        this.mMapView = mMapView;
        this.overlay = overlay;
    }

    @Override
    public void onLocationChanged(Location location) {
        //after the first fix, schedule the task to change the icon
        if (!hasFix){
            TimerTask changeIcon = new TimerTask() {
                @Override
                public void run() {
                    Activity act = activity;
                    if (act!=null)
                        act.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    BitmapDrawable drawable = (BitmapDrawable) activity.getResources().getDrawable(R.drawable.direction_arrow);
                                    overlay.setDirectionArrow(drawable.getBitmap());
                                }catch (Throwable t){
                                    //insultates against crashing when the user rapidly switches fragments/activities
                                }
                            }
                        });

                }
            };
            Timer timer = new Timer();
            timer.schedule(changeIcon, 5000);

        }
        hasFix=true;
        overlay.setBearing(location.getBearing());
        overlay.setAccuracy((int)location.getAccuracy());
        overlay.setLocation(new GeoPoint(location.getLatitude(), location.getLongitude()));
        mMapView.invalidate();
    }
}
