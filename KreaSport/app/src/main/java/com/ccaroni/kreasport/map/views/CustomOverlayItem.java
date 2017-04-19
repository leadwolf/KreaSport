package com.ccaroni.kreasport.map.views;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

/**
 * Created by Master on 05/04/2017.
 */

public class CustomOverlayItem extends OverlayItem {

    private int id;
    private int raceId;

    public CustomOverlayItem(String aTitle, String aSnippet, IGeoPoint aGeoPoint) {
        super(aTitle, aSnippet, aGeoPoint);
    }

    public CustomOverlayItem(String aTitle, String aSnippet, IGeoPoint aGeoPoint, int raceId, int id) {
        super(aTitle, aSnippet, aGeoPoint);
        this.raceId = raceId;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRaceId() {
        return raceId;
    }

    public void setRaceId(int raceId) {
        this.raceId = raceId;
    }
}
