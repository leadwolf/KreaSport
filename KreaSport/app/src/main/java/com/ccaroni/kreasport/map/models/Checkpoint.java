package com.ccaroni.kreasport.map.models;

import java.util.List;

import com.ccaroni.kreasport.map.views.CustomOverlayItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.osmdroid.util.GeoPoint;

public class Checkpoint extends BaseItem {

    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("possibleAnswers")
    @Expose
    private List<String> possibleAnswers = null;
    @SerializedName("location")
    @Expose
    private List<Double> location = null;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getPossibleAnswers() {
        return possibleAnswers;
    }

    public void setPossibleAnswers(List<String> possibleAnswers) {
        this.possibleAnswers = possibleAnswers;
    }

    public List<Double> getLocation() {
        return location;
    }

    public void setLocation(List<Double> location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Checkpoint{" +
                "question='" + question + '\'' +
                ", possibleAnswers=" + possibleAnswers +
                ", location=" + location +
                '}';
    }

    public CustomOverlayItem toCustomOverlayItem() {
        return new CustomOverlayItem(getTitle(), getDescription(), new GeoPoint(getLatitude(), getLongitude()), getId());
    }
}