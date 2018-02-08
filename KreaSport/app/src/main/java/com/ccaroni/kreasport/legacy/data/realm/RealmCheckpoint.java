package com.ccaroni.kreasport.legacy.data.realm;

import com.ccaroni.kreasport.legacy.data.dto.Checkpoint;
import com.ccaroni.kreasport.map.views.CustomOverlayItem;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmCheckpoint extends RealmObject {

    @PrimaryKey
    private String id;
    private String title;
    private String description;
    private Double latitude;
    private Double longitude;

    private RealmRiddle riddle;

    private int order;
    private String raceId;

    public String getId() {
        return id;
    }

    public RealmCheckpoint setId(String id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public RealmCheckpoint setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public RealmCheckpoint setDescription(String description) {
        this.description = description;
        return this;
    }

    public Double getLatitude() {
        return latitude;
    }

    public RealmCheckpoint setLatitude(Double latitude) {
        this.latitude = latitude;
        return this;
    }

    public Double getLongitude() {
        return longitude;
    }

    public RealmCheckpoint setLongitude(Double longitude) {
        this.longitude = longitude;
        return this;
    }

    public String getQuestion() {
        return riddle.getQuestion();
    }


    public RealmList<String> getPossibleAnswers() {
        return riddle.getAnswers();
    }

    public int getOrder() {
        return order;
    }

    public RealmCheckpoint setOrder(int order) {
        this.order = order;
        return this;
    }

    public String getRaceId() {
        return raceId;
    }

    public RealmCheckpoint setRaceId(String raceId) {
        this.raceId = raceId;
        return this;
    }

    public RealmRiddle getRiddle() {
        return riddle;
    }

    public RealmCheckpoint setRealmRiddle(RealmRiddle riddle) {
        this.riddle = riddle;
        return this;
    }

    @Override
    public String toString() {
        return "RealmCheckpoint{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", riddle=" + riddle +
                ", order=" + order +
                ", raceId='" + raceId + '\'' +
                '}';
    }

    public CustomOverlayItem toCustomOverlayItem() {
        return new CustomOverlayItem(getTitle(), getDescription(), extractGeoPoint(), getId(), getRaceId());
    }

    public GeoPoint extractGeoPoint() {
        return new GeoPoint(latitude, longitude);
    }

    public int getAnswerIndex() {
        return riddle.getAnswerIndex();
    }

    public static List<Checkpoint> toDTO(RealmList<RealmCheckpoint> realmCheckpoints) {
        List<Checkpoint> checkpointList = new ArrayList<>();
        for (RealmCheckpoint realmCheckpoint : realmCheckpoints) {
            checkpointList.add(realmCheckpoint.toDTO());
        }
        return checkpointList;
    }

    public Checkpoint toDTO() {
        Checkpoint checkpoint = new Checkpoint();
        checkpoint.setId(getId());
        checkpoint.setTitle(getTitle());
        checkpoint.setDescription(getDescription());
        checkpoint.setLongitude(getLongitude());
        checkpoint.setLatitude(getLatitude());

        checkpoint.setRiddle(getRiddle().toDTO());
        checkpoint.setOrder(getOrder());

        return checkpoint;
    }
}