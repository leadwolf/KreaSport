package fr.univ_lille1.iut_info.caronic.kreasport.maps;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.DirectedLocationOverlay;

/**
 * Created by Master on 30/03/2017.
 */

public class CustomMapView extends MapView {

    private Activity activity;
    private final MapOptions mMapOptions;

    public CustomMapView(Activity activity, MapOptions mMapOptions) {
        super(activity);
        this.activity = activity;
        this.mMapOptions = mMapOptions;

        applyOptions();
    }

    private void applyOptions() {
        if (mMapOptions != null) {
            if (mMapOptions.isEnableLocationOverlay()) {

                DirectedLocationOverlay mLocationOverlay = new DirectedLocationOverlay(activity);
                mLocationOverlay.setShowAccuracy(true);
                getOverlays().add(mLocationOverlay);
            }
            if (mMapOptions.isEnableCompass()) {
                CompassOverlay mCompassOverlay = new CompassOverlay(activity, new InternalCompassOrientationProvider(activity), this);
                mCompassOverlay.enableCompass();
                getOverlays().add(mCompassOverlay);
            }
            if (mMapOptions.isEnableMultiTouchControls()) {
                setMultiTouchControls(true);
            }
            if (mMapOptions.isEnableRotationGesture()) {
                RotationGestureOverlay mRotationGestureOverlay = new RotationGestureOverlay(this);
                mRotationGestureOverlay.setEnabled(true);
                getOverlays().add(mRotationGestureOverlay);
            }
            if (mMapOptions.isEnableScaleOverlay()) {
                ScaleBarOverlay mScaleBarOverlay = new ScaleBarOverlay(this);
                mScaleBarOverlay.setCentred(true);
                //play around with these values to get the location on screen in the right place for your application
                mScaleBarOverlay.setScaleBarOffset(100, 10);
                getOverlays().add(mScaleBarOverlay);
            }
        }
    }

}
