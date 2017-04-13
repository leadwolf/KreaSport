package com.ccaroni.kreasport.rest.api;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;

import java.util.List;

/**
 * Created by Master on 04/04/2017.
 */
@Entity
public class Checkpoint extends BasePoint {

    private ObjectId raceId;
    private String question;
    private List<String> possiblePossibleAnswers;

    public Checkpoint() {
    }

    public Checkpoint(ObjectId id, String title, String description, ObjectId raceId, String question, List<String> possiblePossibleAnswers) {
        super(id, title, description);
        this.raceId = raceId;
        this.question = question;
        this.possiblePossibleAnswers = possiblePossibleAnswers;
    }

    public ObjectId getRaceId() {
        return raceId;
    }

    public void setRaceId(ObjectId raceId) {
        this.raceId = raceId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getPossiblePossibleAnswers() {
        return possiblePossibleAnswers;
    }

    public void setPossiblePossibleAnswers(List<String> possiblePossibleAnswers) {
        this.possiblePossibleAnswers = possiblePossibleAnswers;
    }

}
