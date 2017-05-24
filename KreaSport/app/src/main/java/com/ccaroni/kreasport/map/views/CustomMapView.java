package com.ccaroni.kreasport.map.views;

import android.app.Activity;
import android.support.v4.BuildConfig;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.CopyrightOverlay;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.TilesOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.DirectedLocationOverlay;

import com.ccaroni.kreasport.map.viewmodels.MapVM;
import com.ccaroni.kreasport.map.models.MapOptions;

/**
 * Created by Master on 30/03/2017.
 */

public class CustomMapView extends MapView {

    private static final String LOG = CustomMapView.class.getSimpleName();


    private Activity activity;
    private MapViewCommunication mapViewCommunication;
    private DirectedLocationOverlay mLocationOverlay;

    public CustomMapView(Activity activity, MapOptions mMapOptions, MapVM mMapVM) {
        super(activity);

        if (activity instanceof MapViewCommunication) {
            this.mapViewCommunication = (MapViewCommunication) activity;
        } else {
            throw new RuntimeException(activity + " must implement " + MapViewCommunication.class.getSimpleName());
        }

        this.activity = activity;

        applyBasics();
        applyOptions(mMapOptions);
        applyState(mMapVM);
    }

    private void applyBasics() {
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        setTileSource(TileSourceFactory.MAPNIK);
        getOverlays().add(new CopyrightOverlay(getContext()));

        TilesOverlay x = getOverlayManager().getTilesOverlay();
        x.setOvershootTileCache(x.getOvershootTileCache() * 2);

        setTilesScaledToDpi(true);

        setMinZoomLevel(2);

        addBackgroundTouchListener();
    }

    private void addBackgroundTouchListener() {
        MapEventsOverlay mMapEventOverlay = new MapEventsOverlay(new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                mapViewCommunication.onMapBackgroundTouch();
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        });

        getOverlays().add(0, mMapEventOverlay);
    }

    private void applyState(MapVM mMapVM) {
        getController().setZoom(mMapVM.getZoom());
        getController().setCenter(mMapVM.getCenter());
    }

    @SuppressWarnings({"MissingPermission"})
    private void applyOptions(MapOptions mMapOptions) {
        if (mMapOptions != null) {
            if (mMapOptions.isEnableLocationOverlay()) {

                mLocationOverlay = new DirectedLocationOverlay(activity);
                mLocationOverlay.setShowAccuracy(true);
                mLocationOverlay.setEnabled(true);
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

    public DirectedLocationOverlay getLocationOverlay() {
        return mLocationOverlay;
    }

    public interface MapViewCommunication {
        public void onMapBackgroundTouch();
    }
}
