package com.ccaroni.kreasport.legacy.data.realm;

import android.location.Location;
import android.util.Log;

import com.ccaroni.kreasport.legacy.data.RealmHelper;
import com.ccaroni.kreasport.legacy.data.dto.RaceRecord;

import org.threeten.bp.OffsetDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by Master on 22/05/2017.
 */

public class RealmRaceRecord extends RealmObject {

    @Ignore
    private static final String TAG = RealmRaceRecord.class.getSimpleName();

    private String id;

    private String raceId;
    private String userId;

    private boolean inProgress;

    private long baseTime;
    private long timeExpired;

    private int targetCheckpointIndex;

    private int geofenceProgression;

    private boolean synced;
    private boolean toDelete;


    private String dateTime;
//    private OffsetDateTime dateTime;

    private RealmList<RealmLocation> userPath;

    public RealmRaceRecord() {
        inProgress = false;
        toDelete = false;

        geofenceProgression = -1;

        id = UUID.randomUUID().toString();
        dateTime = OffsetDateTime.now().toString();
        userPath = new RealmList<>();
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return if this record is currently active (the user has started a race and not canceled)
     */
    public boolean isInProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public long getBaseTime() {
        return baseTime;
    }

    public void setBaseTime(long baseTime) {
        this.baseTime = baseTime;
    }

    /**
     * @return the index of the targeting checkpoint. When the user reaches the checkpoint but hasnt yet answered the riddle, the two values will be the same. Otherwise, this
     * should always be one increment ahead of the geofence target index
     */
    public int getTargetCheckpointIndex() {
        return targetCheckpointIndex;
    }

    public void setTargetCheckpointIndex(int targetCheckpointIndex) {
        this.targetCheckpointIndex = targetCheckpointIndex;
    }

    /**
     * @return the index of the checkpoint that the current geofence is targeting. Is supposed to be one increment ahead of {@link #getTargetCheckpointIndex()}
     */
    public int getGeofenceProgression() {
        return geofenceProgression;
    }

    public void setGeofenceProgression(int geofenceProgression) {
        this.geofenceProgression = geofenceProgression;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public long getTimeExpired() {
        return timeExpired;
    }

    public void setTimeExpired(long timeExpired) {
        this.timeExpired = timeExpired;
    }

    public RealmCheckpoint getCurrentCheckpoint(RealmRace realmRace) {
        if (realmRace == null) {
            throw new IllegalArgumentException("Cannot get checkpoint from a null race");
        }
        Log.d(TAG, "finding checkpoint at index " + targetCheckpointIndex + " as current checkpoint for " + realmRace.getId());
        return realmRace.getRealmCheckpoints().get(targetCheckpointIndex);
    }

    @Override
    public String toString() {
        return "RealmRaceRecord{" +
                "id='" + id + '\'' +
                ", raceId='" + raceId + '\'' +
                ", userId='" + userId + '\'' +
                ", inProgress=" + inProgress +
                ", baseTime=" + baseTime +
                ", timeExpired=" + timeExpired +
                ", targetCheckpointIndex=" + targetCheckpointIndex +
                ", geofenceProgression=" + geofenceProgression +
                ", synced=" + synced +
                ", toDelete=" + toDelete +
                ", dateTime='" + dateTime + '\'' +
                '}';
    }

    /**
     * @param inProgress
     * @param directSave if this method should modify the value right here. Set to false if you are going to batch save some values.
     */
    public void setInProgress(boolean inProgress, boolean directSave) {
        if (directSave) {
            RealmHelper.getInstance(null).beginTransaction();
            this.inProgress = inProgress;
            RealmHelper.getInstance(null).commitTransaction();
        } else {
            this.inProgress = inProgress;
        }
    }

    public void incrementTargetCheckpointIndex() {
        targetCheckpointIndex++;
    }

    public void incrementGeofenceProgression() {
        geofenceProgression++;
    }

    public RaceRecord toDTO() {
        return new RaceRecord()
                .setId(id)
                .setRaceId(raceId)
                .setUserId(userId)
                .setTimeExpired(timeExpired)
                .setDateTime(dateTime);
    }

    public void markForDeletion() {
        toDelete = true;
    }

    public boolean isToDelete() {
        return toDelete;
    }

    public RealmList<RealmLocation> getUserPath() {
        return userPath;
    }

    public void setUserPath(RealmList<RealmLocation> userPath) {
        this.userPath = userPath;
    }

    public void addLocationRecord(Location record) {
        RealmLocation realmLocation = RealmLocation.fromLocation(record);
        userPath.add(realmLocation);
        Log.d(TAG, "saved " + realmLocation.toString());
    }

    public List<Location> getUserPathAsSimpleList() {
        List<Location> locations = new ArrayList<>();
        for (RealmLocation realmLocation : userPath) {
            Location location = new Location("user_path");
            location.setLatitude(realmLocation.getLatitude());
            location.setLongitude(realmLocation.getLongitude());
            locations.add(location);
        }
        return locations;
    }

    public Location getLastRecordedLocation() {
        RealmLocation realmLocation = userPath.get(userPath.size() - 1);
        return realmLocation.toLocation();
    }
}
