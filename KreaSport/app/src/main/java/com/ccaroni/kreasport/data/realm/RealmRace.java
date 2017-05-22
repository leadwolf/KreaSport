package com.ccaroni.kreasport.data.realm;

import android.location.Location;
import android.util.Log;

import com.ccaroni.kreasport.data.RaceHelper;
import com.ccaroni.kreasport.data.dto.Checkpoint;
import com.ccaroni.kreasport.map.views.CustomOverlayItem;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmRace extends RealmObject {

    private static final String LOG = RealmRace.class.getSimpleName();


    @PrimaryKey
    private String id;
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

    /**
     * The current checkpoint the user has to reach.
     */
    private int progression;

    /**
     * The time expired since the user started the race.
     */
    private long timeExpired;

    /**
     * The number of times the user has started the race.
     */
    private int attempts;

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

    /**
     * Sets the field inProgress accordingly.
     * <b>AND</b> either increments the number of attempts or resets the time.
     *
     * @param inProgress <b>true:</b> increment the number of attemps<br>
     *                   <b>false:</b> reset the time
     */
    public void setInProgress(boolean inProgress) {
        RaceHelper.getInstance(null).beginTransaction();
        this.inProgress = inProgress;
        RaceHelper.getInstance(null).commitTransaction();
    }

    public void setProgression(int progression) {
        this.progression = progression;
    }

    public int getProgression() {
        return progression;
    }

    public String getProgressionAsString() {
        return "" + progression + "/" + realmCheckpoints.size();
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

    public long getTimeExpired() {
        return timeExpired;
    }

    public void setTimeExpired(long timeExpired) {
        RaceHelper.getInstance(null).beginTransaction();
        Log.d(LOG, "saved time expired");
        this.timeExpired = timeExpired;
        RaceHelper.getInstance(null).commitTransaction();
    }

    public void resetTimeExpired() {
        RaceHelper.getInstance(null).beginTransaction();
        Log.d(LOG, "reset time expired");
        timeExpired = 0;
        RaceHelper.getInstance(null).commitTransaction();
    }

    public int getAttempts() {
        return attempts;
    }

    public void incrementAttempts() {
        RaceHelper.getInstance(null).beginTransaction();
        attempts++;
        RaceHelper.getInstance(null).commitTransaction();
    }

    public CustomOverlayItem toCustomOverlayItemAsSingle() {
        return new CustomOverlayItem(getTitle(), getDescription(), new GeoPoint(getLatitude(), getLongitude()), getId(), getId()).setPrimary(true);
    }

    public List<CustomOverlayItem> toCustomOverlayWithCheckpoints() {
        List<CustomOverlayItem> items = new ArrayList<>();
        items.add(toCustomOverlayItemAsSingle());

        for (RealmCheckpoint realmCheckpoint : realmCheckpoints) {
            items.add(realmCheckpoint.toCustomOverlayItem());
        }

        return items;
    }

    /**
     * @param raceActive      <b>true: </b>Converts all the primary parts of the races to a {@link List} of {@link CustomOverlayItem} with
     *                        ({@link CustomOverlayItem#isPrimary()}).<br>
     *                        <b>false: </b>    Converts the race (as marker) and all its checkpoints to a {@link List} of {@link CustomOverlayItem}
     * @param racesForOverlay
     * @return
     */
    public static List<CustomOverlayItem> racesToOverlay(boolean raceActive, List<RealmRace> racesForOverlay) {
        List<CustomOverlayItem> items = new ArrayList<>();
        if (!raceActive) {
            for (RealmRace realmRace : racesForOverlay) {
                CustomOverlayItem customOverlayItem = realmRace.toCustomOverlayItemAsSingle();

                Log.d(LOG, "converted race to single overlay item");
                Log.d(LOG, "from " + realmRace.getId() + " to " + customOverlayItem.getId());

                items.add(realmRace.toCustomOverlayItemAsSingle());
            }
        } else {
            // race is active, so show the checkpoints too.
            if (racesForOverlay.size() != 1) {
                throw new RuntimeException("Parameter said race was active, but list of races held multiple instead of one");
            }
            RealmRace toConvert = racesForOverlay.get(0);
            items.addAll(toConvert.toCustomOverlayWithCheckpoints());
        }
        return items;
    }

    /**
     * Searches its checkpoints for one that has the same id
     *
     * @param id
     * @return
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

    public RealmCheckpoint getCurrentCheckpoint() {
        return realmCheckpoints.get(progression);
    }
}