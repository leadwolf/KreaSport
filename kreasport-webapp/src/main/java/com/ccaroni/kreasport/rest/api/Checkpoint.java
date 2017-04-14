package com.ccaroni.kreasport.rest.api;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

import java.util.List;

/**
 * Created by Master on 04/04/2017.
 */
public class Checkpoint extends BasePoint {

    @Id
    private String id;
    @Property
    private String question;
    @Embedded
    private List<String> possiblePossibleAnswers;

    public Checkpoint() {
        id = new ObjectId().toString();
    }

    public Checkpoint(String title, String description, String question, double latitude, double longitude, List<String> possiblePossibleAnswers) {
        super(title, description, latitude, longitude);
        this.question = question;
        this.possiblePossibleAnswers = possiblePossibleAnswers;
        id = new ObjectId().toString();
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

    public void addPossibleAnswer(String possibleAnswer) {
        possiblePossibleAnswers.add(possibleAnswer);
    }

}
