package com.ccaroni.kreasport.data.dao;


import com.ccaroni.kreasport.data.base.AbstractCheckpointDTO;
import com.ccaroni.kreasport.data.base.AbstractRaceDTO;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.annotation.Entity;

/**
 * Created by Master on 01/11/2017.
 */

@Entity
public abstract class AbstractRaceDAO<C extends AbstractCheckpointDAO<?>> extends AbstractBaseItemDAO<AbstractRaceDTO<?>> {

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
    public abstract AbstractRaceDTO<?> toDTO();
}
