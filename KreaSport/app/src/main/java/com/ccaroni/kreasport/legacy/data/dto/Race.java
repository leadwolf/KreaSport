package com.ccaroni.kreasport.legacy.data.dto;

import java.util.ArrayList;
import java.util.List;

import com.ccaroni.kreasport.legacy.data.realm.RealmRace;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;

public class Race extends BaseItem {

    @SerializedName("checkpoints")
    @Expose
    private List<Checkpoint> checkpoints = null;

    public Race() {
        super();
        checkpoints = new ArrayList<>();
    }

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(List<Checkpoint> checkpoints) {
        this.checkpoints = checkpoints;
    }

    @Override
    public String toString() {
        return "RealmRace{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                "checkpoints=" + checkpoints +
                '}';
    }

    public RealmRace toRealmRace() {
        RealmRace realmRace = (RealmRace) new RealmRace()
                .setId(getId())
                .setTitle(getTitle())
                .setDescription(getDescription())
                .setLatitude(getLatitude())
                .setLongitude(getLongitude());
        realmRace.setRealmCheckpointsFromNormal(getCheckpoints());
        return realmRace;
    }

    public static RealmList<RealmRace> toRealmList(List<Race> raceList) {
        RealmList<RealmRace> realmRaceList = new RealmList<>();
        for (Race race : raceList) {
            realmRaceList.add(race.toRealmRace());
        }
        return realmRaceList;
    }
}