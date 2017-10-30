package com.ccaroni.kreasport.map.views;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Master on 30/10/2017.
 */

public class CustomPolyline extends Polyline {

    private static final String TAG = CustomPolyline.class.getSimpleName();


    private List<GeoPoint> geoPoints;

    public CustomPolyline() {
        super();
        geoPoints = new ArrayList<>();
    }

    /**
     * Wrapper for {@link #setPoints(List)}. Allows for passing a single {@link GeoPoint} in parameter instead of having to get original points and then setting with a new point
     * in the list
     *
     * @param geoPoint the new {@link GeoPoint} to add to the line
     */
    public void addGeoPoint(GeoPoint geoPoint) {
        geoPoints.add(geoPoint);
        setPoints(geoPoints);
    }

}
