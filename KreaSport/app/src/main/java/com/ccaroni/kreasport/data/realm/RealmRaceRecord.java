package com.ccaroni.kreasport.data.realm;

import android.util.Log;

import com.ccaroni.kreasport.data.RealmHelper;
import com.ccaroni.kreasport.data.dto.RaceRecord;

import org.threeten.bp.OffsetDateTime;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by Master on 22/05/2017.
 */

public class RealmRaceRecord extends RealmObject {

    @Ignore
    private static final String LOG = RealmRaceRecord.class.getSimpleName();


    private String id;

    private String raceId;
    private String userId;

    private boolean started;
    private boolean inProgress;
    private long timeExpired;
    private int progression;

    private int geofenceProgression;

    private boolean synced;
    private boolean toDelete;


    private String dateTime;
//    private OffsetDateTime dateTime;

    public RealmRaceRecord() {
        started = false;
        inProgress = false;
        toDelete = false;
        id = UUID.randomUUID().toString();
        dateTime = OffsetDateTime.now().toString();
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

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public long getTimeExpired() {
        return timeExpired;
    }

    public void setTimeExpired(long timeExpired) {
        this.timeExpired = timeExpired;
    }

    /**
     * @return the index of the last checkpoint the user reached.
     */
    public int getProgression() {
        return progression;
    }

    public void setProgression(int progression) {
        this.progression = progression;
    }

    /**
     * @return the index of the checkpoint that the current geofence is targeting. Is supposed to be one increment ahead of {@link #getProgression()}
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

    public RealmCheckpoint getCurrentCheckpoint(RealmRace realmRace) {
        if (realmRace == null) {
            throw new IllegalArgumentException("Cannot get checkpoint from a null race");
        }
        Log.d(LOG, "finding checkpoint at index " + progression + " as current checkpoint for " + realmRace.getId());
        return realmRace.getRealmCheckpoints().get(progression);
    }

    @Override
    public String toString() {
        return "RealmRaceRecord{" +
                "id='" + id + '\'' +
                ", raceId='" + raceId + '\'' +
                ", userId='" + userId + '\'' +
                ", inProgress=" + inProgress +
                ", timeExpired=" + timeExpired +
                ", progression=" + progression +
                ", geofenceProgression=" + geofenceProgression +
                ", synced=" + synced +
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

    public void setTimeExpired(long timeDifference, boolean directSave) {
        if (directSave) {
            RealmHelper.getInstance(null).beginTransaction();
            this.timeExpired = timeDifference;
            RealmHelper.getInstance(null).commitTransaction();
        } else {
            this.timeExpired = timeDifference;
        }

    }

    public void incrementProgression() {
        progression++;
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
}
