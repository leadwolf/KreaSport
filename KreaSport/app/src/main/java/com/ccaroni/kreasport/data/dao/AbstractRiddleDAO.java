package com.ccaroni.kreasport.data.dao;

import com.ccaroni.kreasport.data.dto.AbstractRiddleDTO;

import java.util.List;

import io.objectbox.annotation.Entity;

/**
 * Created by Master on 01/11/2017.
 */

@Entity
public abstract class AbstractRiddleDAO<T extends AbstractRiddleDTO> implements BaseDAO<T> {

    protected String question;
    protected List<String> possibleAnswers;
    protected int correctAnswerIndex;

    public AbstractRiddleDAO() {
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

    @Override
    public abstract T toDTO();
}
