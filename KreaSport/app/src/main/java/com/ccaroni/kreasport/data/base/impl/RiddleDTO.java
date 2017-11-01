package com.ccaroni.kreasport.data.base.impl;

import com.ccaroni.kreasport.data.base.AbstractRiddleDTO;
import com.ccaroni.kreasport.data.dao.impl.RiddleDAO;

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
