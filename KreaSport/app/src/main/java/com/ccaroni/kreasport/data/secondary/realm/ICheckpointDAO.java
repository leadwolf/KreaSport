package com.ccaroni.kreasport.data.secondary.realm;

import com.ccaroni.kreasport.data.secondary.dto.ICheckpointDTO;

/**
 * Created by Master on 01/11/2017.
 */

public interface ICheckpointDAO<T extends ICheckpointDTO> extends IBaseItemDAO<T> {

    String getRaceID();

    void setRaceID(String raceID);

    int getOrder();

    void setOrder(int order);

    @Override
    T toDTO();
}
