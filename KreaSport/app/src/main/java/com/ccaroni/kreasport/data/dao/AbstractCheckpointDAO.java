package com.ccaroni.kreasport.data.dao;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Index;

/**
 * Created by Master on 01/11/2017.
 */

@Entity
public abstract class AbstractCheckpointDAO extends AbstractBaseItemDAO {

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
}
