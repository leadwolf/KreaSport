package com.ccaroni.kreasport.data.dao;


import com.ccaroni.kreasport.data.dto.AbstractCheckpointDTO;
import com.ccaroni.kreasport.data.dto.AbstractRaceDTO;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.annotation.Entity;

/**
 * Created by Master on 01/11/2017.
 */

@Entity
public abstract class AbstractRaceDAO<C extends AbstractCheckpointDAO<AbstractCheckpointDTO>> extends AbstractBaseItemDAO<AbstractRaceDTO<AbstractCheckpointDTO<?>>> {

    private List<C> checkpoints;

    public AbstractRaceDAO() {
        super();
        checkpoints = new ArrayList<>();
    }

    public List<C> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(List<C> checkpoints) {
        this.checkpoints = checkpoints;
    }

    @Override
    public abstract AbstractRaceDTO<AbstractCheckpointDTO<?>> toDTO();

    protected abstract List<AbstractCheckpointDTO<?>> checkpointsToDTO();
}
