package com.ccaroni.kreasport.map.views;

import android.content.Context;
import android.support.v4.BuildConfig;
import android.util.Log;

import com.ccaroni.kreasport.map.MapDefaults;
import com.ccaroni.kreasport.map.MapOptions;

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

/**
 * Created by Master on 30/03/2017.
 */

public class CustomMapView extends MapView {

    private static final String LOG = CustomMapView.class.getSimpleName();


    private Context context;
    private IMapActivity mapActivity;
    private DirectedLocationOverlay mLocationOverlay;

    /**
     * @param context      the context of the activity or whatever displaying the map
     * @param mapActivity  the activity or whatever displaying the map that will receive callbacks, pass <b>null</b> if don't need the callbacks
     * @param mMapOptions  options for the map
     * @param mMapDefaults defaults for the map
     */
    public CustomMapView(Context context, IMapActivity mapActivity, MapOptions mMapOptions, MapDefaults mMapDefaults) {
        super(context);

        this.context = context;
        if (mapActivity == null) {
            Log.d(LOG, "creating " + this.getClass().getSimpleName() + " without callbacks");
        }
        this.mapActivity = mapActivity;

        applyBasics();
        applyOptions(mMapOptions);
        applyState(mMapDefaults);
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
                mapActivity.onMapBackgroundTouch();
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        });

        getOverlays().add(0, mMapEventOverlay);
    }

    private void applyState(MapDefaults mMapDefaults) {
        if (mMapDefaults != null) {
            getController().setZoom(mMapDefaults.getZoom());
            getController().setCenter(mMapDefaults.getCenter());
        }
    }

    @SuppressWarnings({"MissingPermission"})
    private void applyOptions(MapOptions mMapOptions) {
        if (mMapOptions != null) {
            if (mMapOptions.isEnableLocationOverlay()) {

                mLocationOverlay = new DirectedLocationOverlay(context);
                mLocationOverlay.setShowAccuracy(true);
                mLocationOverlay.setEnabled(true);
                getOverlays().add(mLocationOverlay);
            }

            if (mMapOptions.isEnableCompass()) {
                CompassOverlay mCompassOverlay = new CompassOverlay(context, new InternalCompassOrientationProvider(context), this);
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

    /**
     * All classes using {@link CustomMapView} must implement that interface for callbacks
     */
    public interface IMapActivity {
        void onMapBackgroundTouch();
    }
}
