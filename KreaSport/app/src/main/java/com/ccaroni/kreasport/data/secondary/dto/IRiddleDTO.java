package com.ccaroni.kreasport.data.secondary.dto;

import com.ccaroni.kreasport.data.secondary.realm.IRiddleDAO;
import com.ccaroni.kreasport.data.secondary.realm.impl.RiddleDAO;

import java.util.List;

/**
 * Created by Master on 01/11/2017.
 */

public interface IRiddleDTO<T extends RiddleDAO> extends IBaseDTO<T> {

    String getQuestion();

    void setQuestion(String question);

    List<String> getPossibleAnswers();

    void setPossibleAnswers(List<String> possibleAnswers);

    int getCorrectAnswerIndex();

    void setCorrectAnswerIndex(int correctAnswerIndex);

    @Override
    T toDAO();
}
