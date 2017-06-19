package com.ccaroni.kreasport.domain;

/**
 * Created by Master on 04/04/2017.
 */
public class Checkpoint extends BasePoint {

    private CheckpointKey checkpointKey;
    private Riddle riddle;

    public Checkpoint(){
        super();
    }

    public Checkpoint(String title, String description, double latitude, double longitude) {
        super(title, description, latitude, longitude);
    }

    public Checkpoint(String title, String description, double latitude, double longitude, Riddle riddle, CheckpointKey checkpointkey) {
        super(title, description, latitude, longitude);
        this.riddle = riddle;
        this.checkpointKey=checkpointkey;
    }

    public CheckpointKey getCheckpointKey() {
        return checkpointKey;
    }

    public void setCheckpointKey(CheckpointKey checkpointKey) {
        this.checkpointKey = checkpointKey;
    }

    public Riddle getRiddle() {
        return riddle;
    }

    public void setRiddle(Riddle riddle) {
        this.riddle = riddle;
    }

    @Override
    public String toString() {
        return "Checkpoint{" +
                "checkpointKey=" + checkpointKey +
                ", riddle=" + riddle +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
