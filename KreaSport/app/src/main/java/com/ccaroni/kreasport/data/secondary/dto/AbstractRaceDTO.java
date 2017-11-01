package com.ccaroni.kreasport.data.secondary.dto;


import com.ccaroni.kreasport.data.secondary.dao.AbstractCheckpointDAO;
import com.ccaroni.kreasport.data.secondary.dao.AbstractRaceDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Master on 01/11/2017.
 */

public abstract class AbstractRaceDTO<T extends AbstractCheckpointDTO<?>> extends AbstractBaseItemDTO<AbstractRaceDAO<?>> {

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

    @Override
    public abstract AbstractRaceDAO<?> toDAO();

    protected abstract List<AbstractCheckpointDAO<?>> checkpointsToDTO();
}
