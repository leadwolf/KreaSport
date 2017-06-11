package com.ccaroni.kreasport.domain;

/**
 * Created by Master on 22/05/2017.
 */
public class CheckpointKey {

    private String raceId;
    private int order;

    public CheckpointKey() {
    }

    public CheckpointKey(String raceId, int order) {
        this.raceId = raceId;
        this.order = order;
    }

    public String getRaceId() {
        return raceId;
    }

    public void setRaceId(String raceId) {
        this.raceId = raceId;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
