package com.ccaroni.kreasport.data.realm;

import com.ccaroni.kreasport.data.dto.Checkpoint;
import com.ccaroni.kreasport.map.views.CustomOverlayItem;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmRace extends RealmObject {

    @PrimaryKey
    String id;
    private String title;
    private String description;
    private Double latitude;
    private Double longitude;

    /**
     * If this race has been posted to the server since it was modified.
     */
    private boolean synced;
    /**
     * Whether this is the last race the user was doing. Use to restore after app close.
     */
    private boolean inProgress;

    private RealmList<RealmCheckpoint> realmCheckpoints;


    public String getId() {
        return id;
    }

    public RealmRace setId(String id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public RealmRace setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public RealmRace setDescription(String description) {
        this.description = description;
        return this;
    }

    public Double getLatitude() {
        return latitude;
    }

    public RealmRace setLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public Double getLongitude() {
        return longitude;
    }

    public RealmRace setLongitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public RealmList<RealmCheckpoint> getRealmCheckpoints() {
        return realmCheckpoints;
    }

    public void setRealmCheckpoints(RealmList<RealmCheckpoint> realmCheckpoints) {
        this.realmCheckpoints = realmCheckpoints;
    }

    public void setRealmCheckpointsFromNormal(List<Checkpoint> checkpoints) {
        for (Checkpoint checkpoint : checkpoints) {
            this.realmCheckpoints.add(checkpoint.toRealmCheckpoint());
        }
    }

    @Override
    public String toString() {
        return "RealmRace{" +
                "realmCheckpoints=" + realmCheckpoints +
                '}';
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public static List<CustomOverlayItem> fullRaceToCustomOverlayItem(RealmRace realmRace) {
        if (realmRace.getRealmCheckpoints() != null && realmRace.getRealmCheckpoints().size() > 0) {
            List<CustomOverlayItem> overlayItems = new ArrayList<>();
            for (RealmCheckpoint realmCheckpoint : realmRace.getRealmCheckpoints()) {
                CustomOverlayItem item = realmCheckpoint.toCustomOverlayItem();
                item.setRaceId(realmRace.getId());
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
    public static List<CustomOverlayItem> toPrimaryCustomOverlay(List<RealmRace> racesForOverlay) {
        List<CustomOverlayItem> items = new ArrayList<>();
        for (RealmRace realmRace :racesForOverlay) {
            items.add(new CustomOverlayItem(realmRace.getTitle(), realmRace.getDescription(), new GeoPoint(realmRace.getLatitude(), realmRace.getLongitude()), realmRace.getId()).setPrimary(true));
        }
        return items;
    }
}