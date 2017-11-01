package com.ccaroni.kreasport.data.base;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Master on 01/11/2017.
 */

public abstract class AbstractRaceDTO<T extends AbstractCheckpointDTO> extends AbstractBaseItemDTO {

    private List<T> checkpoints;

    public AbstractRaceDTO() {
        super();
        checkpoints = new ArrayList<>();
    }

    public List<T> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(List<T> checkpoints) {
        this.checkpoints = checkpoints;
    }
}
