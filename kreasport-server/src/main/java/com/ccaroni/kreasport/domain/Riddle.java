package com.ccaroni.kreasport.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Master on 23/05/2017.
 */
public class Riddle {

    private String question;
    private List<String> answers;
    private int answerIndex;

    public Riddle() {
        answers = new ArrayList<>();
    }

    public String getQuestion() {
        return question;
    }

    public Riddle setQuestion(String question) {
        this.question = question;
        return this;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public Riddle setAnswers(List<String> answers) {
        this.answers = answers;
        return this;
    }

    public int getAnswerIndex() {
        return answerIndex;
    }

    public Riddle setAnswerIndex(int answerIndex) {
        this.answerIndex = answerIndex;
        return this;
    }

    @Override
    public String toString() {
        return "Riddle{" +
                "question='" + question + '\'' +
                ", answers=" + answers +
                ", answerIndex=" + answerIndex +
                '}';
    }
}
