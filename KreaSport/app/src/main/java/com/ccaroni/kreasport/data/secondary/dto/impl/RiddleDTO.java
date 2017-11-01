package com.ccaroni.kreasport.data.secondary.dto.impl;

import com.ccaroni.kreasport.data.secondary.realm.impl.RiddleDAO;
import com.ccaroni.kreasport.data.secondary.dto.IRiddleDTO;

import java.util.List;

/**
 * Created by Master on 01/11/2017.
 */

public class RiddleDTO implements IRiddleDTO<RiddleDAO> {

    protected String question;
    protected List<String> possibleAnswers;
    protected int correctAnswerIndex;

    public RiddleDTO() {
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
    public List<String> getPossibleAnswers() {
        return possibleAnswers;
    }

    @Override
    public void setPossibleAnswers(List<String> possibleAnswers) {
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
    public RiddleDAO toDAO() {
        RiddleDAO riddleDAO = new RiddleDAO();
        riddleDAO.setQuestion(getQuestion());
        riddleDAO.setPossibleAnswers(getPossibleAnswers());
        riddleDAO.setCorrectAnswerIndex(getCorrectAnswerIndex());

        return riddleDAO;
    }
}
