package com.ccaroni.kreasport.data.dao.impl;

import com.ccaroni.kreasport.data.dao.AbstractRiddleDAO;

/**
 * Created by Master on 01/11/2017.
 */

public class RiddleCheckpointDAO extends SimpleCheckpointDAO {

    protected AbstractRiddleDAO riddle;

    public RiddleCheckpointDAO() {
        super();
        riddle = new RiddleDAO();
    }

    public AbstractRiddleDAO getRiddle() {
        return riddle;
    }

    public void setRiddle(AbstractRiddleDAO riddle) {
        this.riddle = riddle;
    }
}
