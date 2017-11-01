package com.ccaroni.kreasport.data.base.impl;

import com.ccaroni.kreasport.data.base.AbstractRiddleDTO;

/**
 * Created by Master on 01/11/2017.
 */

public class RiddleCheckpoint extends SimpleCheckpointDTO {

    protected AbstractRiddleDTO riddle;

    public RiddleCheckpoint() {
        super();
        riddle = new RiddleDTO();
    }

    public AbstractRiddleDTO getRiddle() {
        return riddle;
    }

    public void setRiddle(AbstractRiddleDTO riddle) {
        this.riddle = riddle;
    }
}
