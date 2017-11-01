package com.ccaroni.kreasport.data.secondary.dto.impl;

import com.ccaroni.kreasport.data.secondary.dao.impl.RiddleDAO;
import com.ccaroni.kreasport.data.secondary.dto.AbstractRiddleDTO;

/**
 * Created by Master on 01/11/2017.
 */

public class RiddleDTO extends AbstractRiddleDTO<RiddleDAO> {

    public RiddleDTO() {
        super();
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
