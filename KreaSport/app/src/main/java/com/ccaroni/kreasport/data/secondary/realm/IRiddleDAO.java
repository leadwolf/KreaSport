package com.ccaroni.kreasport.data.secondary.realm;

import com.ccaroni.kreasport.data.secondary.dto.IRiddleDTO;

import java.util.List;

import io.objectbox.annotation.Entity;
import io.realm.RealmList;

/**
 * Created by Master on 01/11/2017.
 */

@Entity
public interface IRiddleDAO<T extends IRiddleDTO> extends IBaseDAO<T> {

    String getQuestion();

    void setQuestion(String question);

    RealmList<String> getPossibleAnswers();

    void setPossibleAnswers(RealmList<String> possibleAnswers);

    int getCorrectAnswerIndex();

    void setCorrectAnswerIndex(int correctAnswerIndex);

    @Override
    T toDTO();
}
