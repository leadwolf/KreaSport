package com.ccaroni.kreasport.data.base;

/**
 * Created by Master on 01/11/2017.
 */

public abstract class AbstractCheckpointDTO extends AbstractBaseItemDTO {

    protected String raceID;
    protected int order;

    public AbstractCheckpointDTO() {
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
