package com.ccaroni.kreasport.data.dao;

import com.ccaroni.kreasport.data.dto.AbstractCheckpointDTO;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Index;

/**
 * Created by Master on 01/11/2017.
 */

@Entity
public abstract class AbstractCheckpointDAO<T extends AbstractCheckpointDTO> extends AbstractBaseItemDAO<T> {

    @Index
    protected String raceID;
    protected int order;

    public AbstractCheckpointDAO() {
        super();
    }

    public String getRaceID() {
        return raceID;
    }

    public void setRaceID(String raceID) {
        this.raceID = raceID;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public abstract T toDTO();
}
