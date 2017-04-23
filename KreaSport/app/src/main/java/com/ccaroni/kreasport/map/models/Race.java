package com.ccaroni.kreasport.map.models;

import java.util.ArrayList;
import java.util.List;

import com.ccaroni.kreasport.map.views.CustomOverlayItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.osmdroid.util.GeoPoint;

public class Race extends BaseItem {

    @SerializedName("checkpoints")
    @Expose
    private List<Checkpoint> checkpoints = null;
    @SerializedName("location")
    @Expose
    private List<Double> location = null;

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(List<Checkpoint> checkpoints) {
        this.checkpoints = checkpoints;
    }

    public List<Double> getLocation() {
        return location;
    }

    public void setLocation(List<Double> location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Race{" +
                "checkpoints=" + checkpoints +
                ", location=" + location +
                '}';
    }

    public CustomOverlayItem toCustomOverlayItem() {
        return new CustomOverlayItem(getTitle(), getDescription(), new GeoPoint(getLatitude(), getLongitude()), getId()).setPrimary(true);
    }

    public static List<CustomOverlayItem> fullRaceToCustomOverlayItem(Race race) {
        if (race.getCheckpoints() != null && race.getCheckpoints().size() > 0) {
            List<CustomOverlayItem> overlayItems = new ArrayList<>();
            for (Checkpoint checkpoint : race.getCheckpoints()) {
                CustomOverlayItem item = checkpoint.toCustomOverlayItem();
                item.setRaceId(race.getId());
                overlayItems.add(item);
            }
            return overlayItems;
        } else {
            return null;
        }
    }

}