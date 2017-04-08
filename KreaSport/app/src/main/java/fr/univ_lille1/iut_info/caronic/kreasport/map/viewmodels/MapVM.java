package fr.univ_lille1.iut_info.caronic.kreasport.map.viewmodels;

import org.osmdroid.util.GeoPoint;

import java.io.Serializable;

import fr.univ_lille1.iut_info.caronic.kreasport.map.views.CustomMapView;

/**
 * Created by Master on 05/04/2017.
 */

public class MapVM implements Serializable {

    GeoPoint center;
    int zoom;

    public MapVM(CustomMapView mMapView) {
        center = (GeoPoint) mMapView.getMapCenter();
        zoom = mMapView.getZoomLevel();
    }

    public MapVM(GeoPoint center, int zoom) {
        this.center = center;
        this.zoom = zoom;
    }

    public GeoPoint getCenter() {
        return center;
    }

    public void setCenter(GeoPoint center) {
        this.center = center;
    }

    public int getZoom() {
        return zoom;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }
}
