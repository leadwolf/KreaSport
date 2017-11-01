package com.ccaroni.kreasport.data.dao.impl;

import com.ccaroni.kreasport.data.base.impl.RiddleDTO;
import com.ccaroni.kreasport.data.dao.AbstractRiddleDAO;

/**
 * Created by Master on 01/11/2017.
 */

public class RiddleDAO extends AbstractRiddleDAO<RiddleDTO> {

    public RiddleDAO() {
        super();
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
