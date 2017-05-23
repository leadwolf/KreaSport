package com.ccaroni.kreasport.data.dto;

import com.ccaroni.kreasport.data.realm.RealmRiddle;
import com.ccaroni.kreasport.data.realm.RealmString;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

/**
 * Created by Master on 23/05/2017.
 */

public class Riddle implements Serializable {

    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("answers")
    @Expose
    private List<String> answers;
    @SerializedName("answerIndex")
    @Expose
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

    public static RealmList<RealmString> answersToRealmList(List<String> answers) {
        RealmList<RealmString> realmStrings = new RealmList<>();
        for (String answer : answers) {
            realmStrings.add(new RealmString(answer));
        }
        return realmStrings;
    }
}
