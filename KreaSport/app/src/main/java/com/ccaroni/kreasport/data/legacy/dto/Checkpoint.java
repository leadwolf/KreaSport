package com.ccaroni.kreasport.data.legacy.dto;

import com.ccaroni.kreasport.BR;
import com.ccaroni.kreasport.data.legacy.realm.RealmCheckpoint;

public class Checkpoint extends BaseItem {

    private static final String LOG = Checkpoint.class.getSimpleName();

    private Riddle riddle;
    private int order;

    public Checkpoint() {
        super();
        riddle = new Riddle();
    }

    @Override
    public Checkpoint setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public Checkpoint setTitle(String title) {
        super.setTitle(title);
        return this;
    }

    @Override
    public Checkpoint setDescription(String description) {
        super.setDescription(description);
        notifyPropertyChanged(BR.description);
        return this;
    }

    public Riddle getRiddle() {
        return riddle;
    }

    public Checkpoint setRiddle(Riddle riddle) {
        this.riddle = riddle;
        return this;
    }

    @Override
    public Checkpoint setLatitude(Double latitude) {
        super.setLatitude(latitude);
        return this;
    }


    @Override
    public Checkpoint setLongitude(Double longitude) {
        super.setLongitude(longitude);
        return this;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public RealmCheckpoint toRealmCheckpoint(String raceId) {
        RealmCheckpoint realmCheckpoint = (RealmCheckpoint) new RealmCheckpoint()
                .setId(getId())
                .setTitle(getTitle())
                .setDescription(getDescription())
                .setLatitude(getLatitude())
                .setLongitude(getLongitude());
        realmCheckpoint
                .setRealmRiddle(riddle.toRealmRiddle())
                .setOrder(getOrder())
                .setRaceId(raceId);
        return realmCheckpoint;

    }
}