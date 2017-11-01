package com.ccaroni.kreasport.data.secondary.dto;

import com.ccaroni.kreasport.data.secondary.realm.ICheckpointDAO;

/**
 * Created by Master on 01/11/2017.
 */

public interface ICheckpointDTO<T extends ICheckpointDAO> extends IBaseItemDTO<T> {

    String getRaceID();

    void setRaceID(String raceID);

    int getOrder();

    void setOrder(int order);

    @Override
    T toDAO();
}
