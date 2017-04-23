package com.ccaroni.kreasport.map.views;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

/**
 * Created by Master on 05/04/2017.
 */

public class CustomOverlayItem extends OverlayItem {

    private String id;
    private String raceId;
    private boolean primary;

    public CustomOverlayItem(String aTitle, String aSnippet, IGeoPoint aGeoPoint) {
        super(aTitle, aSnippet, aGeoPoint);
    }

    public CustomOverlayItem(String aTitle, String aSnippet, IGeoPoint aGeoPoint, String id) {
        super(aTitle, aSnippet, aGeoPoint);
        this.id = id;
    }

    public CustomOverlayItem(String aTitle, String aSnippet, IGeoPoint aGeoPoint, String id, String raceId) {
        super(aTitle, aSnippet, aGeoPoint);
        this.id = id;
        this.raceId = raceId;
    }

    public CustomOverlayItem(String aUid, String aTitle, String aDescription, IGeoPoint aGeoPoint, String id, String raceId) {
        super(aUid, aTitle, aDescription, aGeoPoint);
        this.id = id;
        this.raceId = raceId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRaceId() {
        return raceId;
    }

    public void setRaceId(String raceId) {
        this.raceId = raceId;
    }

    public boolean isPrimary() {
        return primary;
    }

    public CustomOverlayItem setPrimary(boolean primary) {
        this.primary = primary;
        return this;
    }
}
