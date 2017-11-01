package com.ccaroni.kreasport.data.base.impl;

import com.ccaroni.kreasport.data.base.AbstractRiddleDTO;

/**
 * Created by Master on 01/11/2017.
 */

public class RiddleCheckpointDTO extends SimpleCheckpointDTO {

    protected AbstractRiddleDTO riddle;

    public RiddleCheckpointDTO() {
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
