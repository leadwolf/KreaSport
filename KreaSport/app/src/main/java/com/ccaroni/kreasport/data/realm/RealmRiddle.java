package com.ccaroni.kreasport.data.realm;

import com.ccaroni.kreasport.data.dto.Riddle;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Master on 23/05/2017.
 */

public class RealmRiddle extends RealmObject {

    private String question;
    private RealmList<RealmString> answers;
    private int answerIndex;

    public RealmRiddle() {
        answers = new RealmList<>();
    }

    public String getQuestion() {
        return question;
    }

    public RealmRiddle setQuestion(String question) {
        this.question = question;
        return this;
    }

    public RealmList<RealmString> getAnswers() {
        return answers;
    }

    public RealmRiddle setAnswers(RealmList<RealmString> answers) {
        this.answers = answers;
        return this;
    }

    public int getAnswerIndex() {
        return answerIndex;
    }

    public RealmRiddle setAnswerIndex(int answerIndex) {
        this.answerIndex = answerIndex;
        return this;
    }

    public Riddle toDTO() {
        return new Riddle()
                .setQuestion(getQuestion())
                .setAnswers(answersToStringList(getAnswers()))
                .setAnswerIndex(getAnswerIndex());

    }

    private static List<String> answersToStringList(RealmList<RealmString> realmAnswers) {
        List<String> answers = new ArrayList<>();

        for (RealmString realmString : realmAnswers) {
            answers.add(realmString.getString());
        }

        return answers;
    }
}
