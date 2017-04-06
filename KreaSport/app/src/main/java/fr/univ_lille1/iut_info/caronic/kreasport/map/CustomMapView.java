package fr.univ_lille1.iut_info.caronic.kreasport.map;

import android.app.Activity;
import android.support.v4.BuildConfig;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.CopyrightOverlay;
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

    private Activity activity;

    public CustomMapView(Activity activity, MapOptions mMapOptions, MapState mMapState) {
        super(activity);
        this.activity = activity;

        applyBasics();
        applyOptions(mMapOptions);
        applyState(mMapState);
    }

    private void applyBasics() {
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        setTileSource(TileSourceFactory.MAPNIK);
        getOverlays().add(new CopyrightOverlay(getContext()));

        TilesOverlay x = getOverlayManager().getTilesOverlay();
        x.setOvershootTileCache(x.getOvershootTileCache() * 2);

        setTilesScaledToDpi(true);

        setMinZoomLevel(2);
    }

    private void applyState(MapState mMapState) {
        getController().setZoom(mMapState.getZoom());
        getController().setCenter(mMapState.getCenter());
    }

    private void applyOptions(MapOptions mMapOptions) {
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
