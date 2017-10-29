package com.ccaroni.kreasport.data.realm;

import android.location.Location;
import android.util.Log;

import com.ccaroni.kreasport.data.dto.BaseItem;
import com.ccaroni.kreasport.data.dto.Checkpoint;
import com.ccaroni.kreasport.data.dto.Race;
import com.ccaroni.kreasport.map.views.CustomOverlayItem;

import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class RealmRace extends RealmObject {

    @Ignore
    private static final String LOG = RealmRace.class.getSimpleName();


    @PrimaryKey
    private String id;
    private String title;
    private String description;
    private Double latitude;
    private Double longitude;


    private RealmList<RealmCheckpoint> realmCheckpoints;

    public RealmRace() {
        realmCheckpoints = new RealmList<>();
    }

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
            this.realmCheckpoints.add(checkpoint.toRealmCheckpoint(getId()));
        }
    }

    @Override
    public String toString() {
        StringBuilder checkpointString = new StringBuilder();
        for (RealmCheckpoint realmCheckpoint : realmCheckpoints) {
            checkpointString.append(realmCheckpoint.toString());
        }
        return "RealmRace{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", realmCheckpoints=" + checkpointString +
                '}';
    }

    public CustomOverlayItem toCustomOverlayItemAsSingle() {
        return new CustomOverlayItem(getTitle(), getDescription(), new GeoPoint(getLatitude(), getLongitude()), getId(), getId()).setPrimary(true);
    }

    /**
     * Adds a marker for the race and a marker for every checkpoint up to and including lastCheckpointIndex
     *
     * @param lastCheckpointIndex the last index to include in the list
     * @return a list of {@link CustomOverlayItem} for all the markers in this race, up to and including lastCheckpointIndex
     */
    public List<CustomOverlayItem> toCustomOverlayWithCheckpoints(int lastCheckpointIndex) {
        List<CustomOverlayItem> items = new ArrayList<>();
        items.add(toCustomOverlayItemAsSingle());

        for (int i = 0; i <= lastCheckpointIndex; i++) {
            RealmCheckpoint realmCheckpoint = realmCheckpoints.get(i);
            items.add(realmCheckpoint.toCustomOverlayItem());
            Log.d(LOG, "converted checkpoint for progression: " + i);
        }

        return items;
    }

    /**
     * @param racesForOverlay
     * @return
     */
    public static List<CustomOverlayItem> racesToOverlay(List<RealmRace> racesForOverlay) {
        List<CustomOverlayItem> items = new ArrayList<>();

        for (RealmRace realmRace : racesForOverlay) {
            CustomOverlayItem customOverlayItem = realmRace.toCustomOverlayItemAsSingle();

            Log.d(LOG, "converted race to single overlay item");
            Log.d(LOG, "from " + realmRace.getId() + " to " + customOverlayItem.getId());

            items.add(realmRace.toCustomOverlayItemAsSingle());
        }

        return items;
    }

    /**
     * Searches its checkpoints for one that has the same id
     *
     * @param id
     * @return the checkpoint with the same id, or null
     */
    public RealmCheckpoint getCheckpointById(String id) {
        for (RealmCheckpoint realmCheckpoint : realmCheckpoints) {
            if (realmCheckpoint.getId().equals(id)) {
                return realmCheckpoint;
            }
        }
        return null;
    }

    public Location getLocation() {
        Location raceStart = new Location("race_start");
        raceStart.setLatitude(latitude);
        raceStart.setLongitude(longitude);

        return raceStart;
    }

    public int getNbCheckpoints() {
        return realmCheckpoints.size();
    }

    public RealmCheckpoint getCheckpointByProgression(int progression) {
        return realmCheckpoints.get(progression);
    }

    /**
     * @param progression the index of what checkpoint we want to compare to
     * @param indexName
     * @return if progression == realmCheckpoint.size() - 1
     */
    public boolean isOnLastCheckpoint(int progression, String indexName) {
        Log.d(LOG, "finish check from 0: nbCheckpoints: " + (realmCheckpoints.size() - 1) + ", " + indexName + " " + progression);
        return progression == realmCheckpoints.size() - 1;
    }

    private List<GeoPoint> getCheckpointGeoPoints() {
        List<GeoPoint> geoPoints = new ArrayList<>();
        for (RealmCheckpoint realmCheckpoint : realmCheckpoints) {
            geoPoints.add(new GeoPoint(realmCheckpoint.getLatitude(), realmCheckpoint.getLongitude()));
        }
        return geoPoints;
    }

    public BoundingBox getBoundingBox() {
        return BoundingBox.fromGeoPoints(getCheckpointGeoPoints());
    }


    public BaseItem toDTO() {
        Race race = new Race();
        race.setId(getId());
        race.setTitle(getTitle());
        race.setDescription(getDescription());
        race.setLongitude(getLongitude());
        race.setLatitude(getLatitude());

        race.setCheckpoints(RealmCheckpoint.toDTO(getRealmCheckpoints()));

        return race;
    }
}