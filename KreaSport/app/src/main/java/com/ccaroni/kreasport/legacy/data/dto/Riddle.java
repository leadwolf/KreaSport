package com.ccaroni.kreasport.legacy.data.dto;

import com.ccaroni.kreasport.legacy.data.realm.RealmRiddle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

/**
 * Created by Master on 23/05/2017.
 */

public class Riddle implements Serializable {

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

    public RealmRiddle toRealmRiddle() {
        return new RealmRiddle()
                .setQuestion(getQuestion())
                .setAnswers(answersToRealmList(getAnswers()))
                .setAnswerIndex(getAnswerIndex());
    }

    public static RealmList<String> answersToRealmList(List<String> answers) {
        RealmList<String> realmStrings = new RealmList<String>();
        realmStrings.addAll(answers);
        return realmStrings;
    }
}
