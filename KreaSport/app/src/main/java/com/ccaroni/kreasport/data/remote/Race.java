package com.ccaroni.kreasport.data.remote;

import com.ccaroni.kreasport.data.model.IRace;

import java.util.List;


/**
 * Created by Master on 09/02/2018.
 */

public class Race extends MapItem implements IRace {

    private List<Checkpoint> checkpoints;

    public Race() {
    }

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(List<Checkpoint> checkpoints) {
        this.checkpoints = checkpoints;
    }
}
