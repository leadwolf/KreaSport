package com.ccaroni.kreasport.data.secondary.realm.impl;

import com.ccaroni.kreasport.data.secondary.realm.IRiddleDAO;
import com.ccaroni.kreasport.data.secondary.dto.impl.RiddleDTO;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Master on 01/11/2017.
 */

public class RiddleDAO extends RealmObject implements IRiddleDAO<RiddleDTO> {

    protected String question;
    protected RealmList<String> possibleAnswers;
    protected int correctAnswerIndex;

    public RiddleDAO() {
        possibleAnswers = new RealmList<>();
    }

    @Override
    public String getQuestion() {
        return question;
    }

    @Override
    public void setQuestion(String question) {
        this.question = question;
    }

    @Override
    public RealmList<String> getPossibleAnswers() {
        return possibleAnswers;
    }

    @Override
    public void setPossibleAnswers(RealmList<String> possibleAnswers) {
        this.possibleAnswers = possibleAnswers;
    }

    @Override
    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    @Override
    public void setCorrectAnswerIndex(int correctAnswerIndex) {
        this.correctAnswerIndex = correctAnswerIndex;
    }

    @Override
    public RiddleDTO toDTO() {
        RiddleDTO riddleDTO = new RiddleDTO();
        riddleDTO.setQuestion(getQuestion());
        riddleDTO.setPossibleAnswers(getPossibleAnswers());
        riddleDTO.setCorrectAnswerIndex(getCorrectAnswerIndex());

        return riddleDTO;
    }
}
