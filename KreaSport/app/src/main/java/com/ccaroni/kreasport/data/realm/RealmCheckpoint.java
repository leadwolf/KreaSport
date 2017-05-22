package com.ccaroni.kreasport.data.realm;

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

    private CheckpointKey checkpointKey;
    private String question;
    private RealmList<RealmString> possibleAnswers;

    public RealmCheckpoint() {
        possibleAnswers = new RealmList<>();
    }


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
        return question;
    }

    public RealmCheckpoint setQuestion(String question) {
        this.question = question;
        return this;
    }

    public RealmList<RealmString> getPossibleAnswers() {
        return possibleAnswers;
    }

    public RealmCheckpoint setPossibleAnswers(RealmList<RealmString> possibleAnswers) {
        this.possibleAnswers = possibleAnswers;
        return this;
    }

    public RealmCheckpoint setPossibleAnswersFromStrings(List<String> possibleAnswers) {
        for (String answer : possibleAnswers) {
            this.possibleAnswers.add(new RealmString(answer));
        }
        return this;
    }

    public CheckpointKey getCheckpointKey() {
        return checkpointKey;
    }

    public RealmCheckpoint setCheckpointKey(CheckpointKey checkpointKey) {
        this.checkpointKey = checkpointKey;
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
                ", checkpointKey=" + checkpointKey +
                ", question='" + question + '\'' +
                ", possibleAnswers=" + possibleAnswers +
                '}';
    }

    public CustomOverlayItem toCustomOverlayItem() {
        return new CustomOverlayItem(getTitle(), getDescription(), extrackGeoPoint(), getId(), checkpointKey.getRaceId());
    }

    public List<String> getPossibleAnswersAsStrings() {
        List<String> possibleAnswersAsStrings = new ArrayList<>();
        for (RealmString realmString : possibleAnswers) {
            possibleAnswersAsStrings.add(realmString.getString());
        }
        return possibleAnswersAsStrings;
    }

    public GeoPoint extrackGeoPoint() {
        return new GeoPoint(latitude, longitude);
    }
}