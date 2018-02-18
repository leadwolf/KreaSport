package com.ccaroni.kreasport.race.model.impl;

import android.location.Location;
import android.os.SystemClock;

import com.ccaroni.kreasport.data.local.Checkpoint;
import com.ccaroni.kreasport.data.local.Race;
import com.ccaroni.kreasport.data.local.Record;
import com.ccaroni.kreasport.race.exception.IllegalRaceStateException;
import com.ccaroni.kreasport.race.model.IRaceModel;
import com.ccaroni.kreasport.race.view.activity.App;

import javax.inject.Inject;

import io.objectbox.Box;

/**
 * Created by Master on 18/02/2018.
 */

public class RaceModel implements IRaceModel {

    private static final String TAG = RaceModel.class.getSimpleName();


    @Inject
    public Box<Race> raceBox;
    @Inject
    public Box<Checkpoint> checkpointBox;
    @Inject
    public Box<Record> recordBox;

    private Race race;
    private Checkpoint checkpoint;
    private Record record;

    public RaceModel() {
        App.getBoxComponent().inject(this);
    }

    @Override
    public void requestStartRace(long raceId) throws IllegalRaceStateException {
        checkCanStartRecording();
        loadRace(raceId);

        startRecording(raceId, getUserId(), SystemClock.elapsedRealtime());
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

    /**
     * @throws IllegalRaceStateException if a race is being recorded
     */
    private void checkCanStartRecording() throws IllegalRaceStateException {
        if (record != null && record.isInProgress()) {
            throw new IllegalRaceStateException("Record already in progress");
        }
    }

    /**
     * @throws IllegalRaceStateException if a race is being recorded
     */
    private void checkCanStopRecording() throws IllegalRaceStateException {
        if (record == null || !record.isInProgress()) {
            throw new IllegalRaceStateException("No record in progress");
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

    private String getUserId() {
        // TODO with CredentialsManager
        return null;
    }

    @Override
    public void requestStopRace() throws IllegalRaceStateException {
        checkCanStopRecording();

        stopRecording();
        saveRecord();
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
    public void updateLocation(Location location) {
        // TODO

    }

    @Override
    public void triggerGeofence(long id) {
        // TODO

    }
}
