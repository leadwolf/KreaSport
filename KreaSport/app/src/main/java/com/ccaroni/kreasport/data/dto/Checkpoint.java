package com.ccaroni.kreasport.data.dto;

import com.ccaroni.kreasport.BR;
import com.ccaroni.kreasport.data.realm.CheckpointKey;
import com.ccaroni.kreasport.data.realm.RealmCheckpoint;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Checkpoint extends BaseItem {

    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("checkpointKey")
    @Expose
    private CheckpointKey checkpointKey;
    @SerializedName("possibleAnswers")
    @Expose
    private List<String> possibleAnswers;
    @SerializedName("location")
    @Expose
    private List<Double> location = null;

    public Checkpoint() {
        super();
        possibleAnswers = new ArrayList<>();
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

    public String getQuestion() {
        return question;
    }

    public Checkpoint setQuestion(String question) {
        this.question = question;
        return this;
    }

    public List<String> getPossibleAnswers() {
        return possibleAnswers;
    }

    public Checkpoint setPossibleAnswers(List<String> possibleAnswers) {
        this.possibleAnswers = possibleAnswers;
        return this;
    }

    public List<Double> getLocation() {
        return location;
    }

    public void setLocation(List<Double> location) {
        this.location = location;
    }

    public CheckpointKey getCheckpointKey() {
        return checkpointKey;
    }

    public void setCheckpointKey(CheckpointKey checkpointKey) {
        this.checkpointKey = checkpointKey;
    }

    @Override
    public String toString() {
        return "Checkpoint{" +
                "question='" + question + '\'' +
                ", checkpointKey=" + checkpointKey +
                ", possibleAnswers=" + possibleAnswers +
                ", location=" + location +
                '}';
    }

    public RealmCheckpoint toRealmCheckpoint() {
        RealmCheckpoint realmCheckpoint = (RealmCheckpoint) new RealmCheckpoint()
                .setId(getId())
                .setTitle(getTitle())
                .setDescription(getDescription())
                .setLatitude(getLatitude())
                .setLongitude(getLongitude());
        realmCheckpoint
                .setCheckpointKey(checkpointKey)
                .setQuestion(getQuestion())
                .setPossibleAnswersFromStrings(getPossibleAnswers());
        return realmCheckpoint;

    }
}