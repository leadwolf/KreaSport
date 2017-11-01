package com.ccaroni.kreasport.data.base;

import java.util.List;

/**
 * Created by Master on 01/11/2017.
 */

public abstract class AbstractRiddleDTO implements BaseDTO {

    protected String question;
    protected List<String> possibleAnswers;
    protected int correctAnswerIndex;

    public AbstractRiddleDTO() {
    }

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

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public void setCorrectAnswerIndex(int correctAnswerIndex) {
        this.correctAnswerIndex = correctAnswerIndex;
    }
}
