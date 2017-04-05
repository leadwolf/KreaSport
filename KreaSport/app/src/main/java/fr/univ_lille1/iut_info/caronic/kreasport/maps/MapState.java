package fr.univ_lille1.iut_info.caronic.kreasport.maps;

import org.osmdroid.util.GeoPoint;

import java.io.Serializable;

/**
 * Created by Master on 05/04/2017.
 */

public class MapState implements Serializable {

    GeoPoint center;
    int zoom;

    public MapState(CustomMapView mMapView) {
        center = (GeoPoint) mMapView.getMapCenter();
        zoom = mMapView.getZoomLevel();
    }

    public MapState(GeoPoint center, int zoom) {
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
