package com.ccaroni.kreasport.map.models;

import java.util.ArrayList;
import java.util.List;

import com.ccaroni.kreasport.data.pojo.RealmRace;
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
        return "RealmRace{" +
                "checkpoints=" + checkpoints +
                ", location=" + location +
                '}';
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

    public CustomOverlayItem primaryToCustomOverlayItem() {
        return new CustomOverlayItem(getTitle(), getDescription(), new GeoPoint(getLatitude(), getLongitude()), getId()).setPrimary(true);
    }

    /**
     * Converts all the primary parts of the races to a {@link CustomOverlayItem} with ({@link CustomOverlayItem#isPrimary()}).
     * @param racesForOverlay
     * @return
     */
    public static List<CustomOverlayItem> toPrimaryCustomOverlay(List<Race> racesForOverlay) {
        List<CustomOverlayItem> items = new ArrayList<>();
        for (Race race :racesForOverlay) {
            items.add(new CustomOverlayItem(race.getTitle(), race.getDescription(), new GeoPoint(race.getLatitude(), race.getLongitude()), race.getId()).setPrimary(true));
        }
        return items;
    }

    public RealmRace toRealmRace() {
        RealmRace realmRace = (RealmRace) new RealmRace()
                .setId(getId())
                .setTitle(getTitle())
                .setDescription(getDescription())
                .setLatitude(getLatitude())
                .setLongitude(getLongitude());
        realmRace.setRealmCheckpointsFromNormal(getCheckpoints());
        return realmRace;
    }
}