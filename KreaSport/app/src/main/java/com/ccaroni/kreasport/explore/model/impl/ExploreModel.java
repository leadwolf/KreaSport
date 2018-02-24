package com.ccaroni.kreasport.explore.model.impl;

import android.location.Location;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ccaroni.kreasport.data.local.Checkpoint;
import com.ccaroni.kreasport.data.local.MapItem;
import com.ccaroni.kreasport.data.local.Race;
import com.ccaroni.kreasport.data.local.Record;
import com.ccaroni.kreasport.explore.exception.IllegalRaceStateException;
import com.ccaroni.kreasport.explore.model.IExploreModel;
import com.ccaroni.kreasport.explore.model.RaceModelListener;

import io.objectbox.Box;

/**
 * Created by Master on 18/02/2018.
 */

public class ExploreModel implements IExploreModel {

    private static final String TAG = ExploreModel.class.getSimpleName();


    private Box<Race> raceBox;
    private Box<Checkpoint> checkpointBox;
    private Box<Record> recordBox;

    @Nullable
    private Race race;
    @Nullable
    private Checkpoint checkpoint;
    @Nullable
    private Record record;

    /**
     * The race or checkpoint that is currently selected and whose information is displayed in the bottom bar
     */
    @Nullable
    private MapItem selectedItem;

    private RaceModelListener raceModelListener;

    public ExploreModel(@NonNull Box<Race> raceBox, @NonNull Box<Checkpoint> checkpointBox, @NonNull Box<Record> recordBox) {
        this.raceBox = raceBox;
        this.checkpointBox = checkpointBox;
        this.recordBox = recordBox;
    }

    @NonNull
    @Override
    public String getTitle() {
        return selectedItem == null ? "" : selectedItem.getTitle();
        // TODO when the user is recording, should this be the targeting checkpoint info?
    }

    @NonNull
    @Override
    public String getDescription() {
        return selectedItem == null ? "" : selectedItem.getDescription();
    }

    @Override
    public void requestStartRace() throws IllegalRaceStateException {
        verifyNotRecording();
        if (selectedItem == null) {
            throw new IllegalRaceStateException("No item is selected");
        }

        loadRace(selectedItem.getId());
        verifyProximityToStart();

        startRecording(this.race.getId(), this.getUserId(), SystemClock.elapsedRealtime());
    }

    /**
     * @throws IllegalRaceStateException if a race is being recorded
     */
    private void verifyNotRecording() throws IllegalRaceStateException {
        if (record != null && record.isInProgress()) {
            throw new IllegalRaceStateException("Record already in progress");
        }
    }

    /**
     * @param id the id of the race to load into {@link #race}
     * @throws IllegalRaceStateException if the race was not found
     */
    private void loadRace(long id) throws IllegalRaceStateException {
        this.race = this.raceBox.get(id);
        if (this.race == null) {
            throw new IllegalRaceStateException("Race not found");
        }
    }

    private void verifyProximityToStart() {
        // TODO
        // verify location according to ExploreService
    }

    /**
     * Created a new record and sets the base time to {@link SystemClock#elapsedRealtime()}
     *
     * @param raceId   the id of the race to record
     * @param userId   the id of the user currently logged in
     * @param baseTime the time to set as the start of the recording
     */
    private void startRecording(long raceId, String userId, long baseTime) {
        this.record = new Record(raceId, userId);
        this.record.setBaseTime(baseTime);
    }

    private String getUserId() {
        return this.raceModelListener.getUserId();
    }

    @Override
    public void requestStopRace() throws IllegalRaceStateException {
        verifyIsRecording();

        stopRecording();
        saveRecord();
    }

    /**
     * @throws IllegalRaceStateException if a race is not being recorded
     */
    private void verifyIsRecording() throws IllegalRaceStateException {
        if (this.record == null || !this.record.isInProgress()) {
            throw new IllegalRaceStateException("No record in progress");
        }
    }

    /**
     * Sets the end time to the record
     */
    private void stopRecording() {
        long elapsedTime = SystemClock.elapsedRealtime() - this.record.getBaseTime();
        this.record.setTimeExpired(elapsedTime);
    }

    private void saveRecord() {
        this.recordBox.put(this.record);
    }

    @Override
    public void updateLocation(Location location) throws IllegalRaceStateException {
        verifyIsRecording();
        this.record.addLocation(location);
    }

    @Override
    public void onGeofenceTriggered(long id) {
        // TODO
        // verify progression
        // notify new progress
    }

    @Override
    public String getProgression() {
        try {
            verifyIsRecording();
        } catch (IllegalRaceStateException e) {
            return "";
        }

        int target = this.record.getTargetCheckpointIndex();
        return (target - 1) + "/" + this.race.getNbCheckpoints();
    }

    @Override
    public void onRaceSelected(long id) {
        this.selectedItem = raceBox.get(id);
    }

    @Override
    public void onCheckpointSelected(long id) {
        this.selectedItem = checkpointBox.get(id);
    }

    @Override
    public boolean isRaceActive() {
        return this.record != null && this.record.isInProgress();
    }

    @Override
    public boolean isItemSelected() {
        return this.selectedItem != null;
    }

    @Override
    public void onBackgroundPressed() {
        this.selectedItem = null;
    }
}
