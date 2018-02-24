package com.ccaroni.kreasport.explore.model.impl;

import android.location.Location;
import android.os.SystemClock;
import android.support.annotation.NonNull;

import com.ccaroni.kreasport.data.local.Checkpoint;
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

    private Race race;
    private Checkpoint checkpoint;
    private Record record;

    private RaceModelListener raceModelListener;

    public ExploreModel(Box<Race> raceBox, Box<Checkpoint> checkpointBox, Box<Record> recordBox) {
        this.raceBox = raceBox;
        this.checkpointBox = checkpointBox;
        this.recordBox = recordBox;
    }

    @NonNull
    @Override
    public String getTitle() {
        // TODO
        return "";
    }

    @NonNull
    @Override
    public String getDescription() {
        // TODO
        return "";
    }

    @Override
    public void requestStartRace(long raceId) throws IllegalRaceStateException {
        verifyNotRecording();
        loadRace(raceId);
        verifyProximityToStart();

        startRecording(raceId, this.getUserId(), SystemClock.elapsedRealtime());
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
        race = raceBox.get(id);
        if (race == null) {
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
        record = new Record(raceId, userId);
        record.setBaseTime(baseTime);
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
        if (record == null || !record.isInProgress()) {
            throw new IllegalRaceStateException("No record in progress");
        }
    }

    /**
     * Sets the end time to the record
     */
    private void stopRecording() {
        long elapsedTime = SystemClock.elapsedRealtime() - record.getBaseTime();
        record.setTimeExpired(elapsedTime);
    }

    private void saveRecord() {
        recordBox.put(record);
    }

    @Override
    public void updateLocation(Location location) throws IllegalRaceStateException {
        verifyIsRecording();
        record.addLocation(location);
    }

    @Override
    public void onGeofenceTriggered(long id) {
        // TODO
        // verify progression
        // notify new progress
    }
}
