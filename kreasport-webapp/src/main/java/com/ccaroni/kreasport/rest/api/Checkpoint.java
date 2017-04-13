package com.ccaroni.kreasport.rest.api;

import org.mongodb.morphia.annotations.Embedded;

import java.util.List;

/**
 * Created by Master on 04/04/2017.
 */
public class Checkpoint extends BasePoint {

    private String question;
    @Embedded
    private List<String> possiblePossibleAnswers;

    public Checkpoint() {
    }

    public Checkpoint(String title, String description, String question, List<String> possiblePossibleAnswers) {
        super(title, description);
        this.question = question;
        this.possiblePossibleAnswers = possiblePossibleAnswers;
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
