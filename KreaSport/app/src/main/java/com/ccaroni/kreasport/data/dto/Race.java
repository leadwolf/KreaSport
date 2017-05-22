package com.ccaroni.kreasport.data.dto;

import java.util.ArrayList;
import java.util.List;

import com.ccaroni.kreasport.data.realm.RealmRace;
import com.ccaroni.kreasport.map.views.CustomOverlayItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.osmdroid.util.GeoPoint;

import io.realm.RealmList;

public class Race extends BaseItem {

    @SerializedName("checkpoints")
    @Expose
    private List<Checkpoint> checkpoints = null;
    @SerializedName("location")
    @Expose
    private List<Double> location = null;

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

    public List<Double> getLocation() {
        return location;
    }

    public void setLocation(List<Double> location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "RealmRace{" +
                "checkpoints=" + checkpoints +
                ", location=" + location +
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