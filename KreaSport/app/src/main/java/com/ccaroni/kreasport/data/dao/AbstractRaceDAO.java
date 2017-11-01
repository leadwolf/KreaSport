package com.ccaroni.kreasport.data.dao;


import java.util.ArrayList;
import java.util.List;

import io.objectbox.annotation.Entity;

/**
 * Created by Master on 01/11/2017.
 */

@Entity
public abstract class AbstractRaceDAO<T extends AbstractCheckpointDAO> extends AbstractBaseItemDAO {

    private List<T> checkpoints;

    public AbstractRaceDAO() {
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
