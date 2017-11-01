package com.ccaroni.kreasport.data.dao.impl;

import com.ccaroni.kreasport.data.base.impl.SimpleCheckpointDTO;
import com.ccaroni.kreasport.data.dao.AbstractCheckpointDAO;

/**
 * Created by Master on 01/11/2017.
 */

public class SimpleCheckpointDAO extends AbstractCheckpointDAO<SimpleCheckpointDTO> {

    public SimpleCheckpointDAO() {
        super();
    }

    @Override
    public SimpleCheckpointDTO toDTO() {
        SimpleCheckpointDTO simpleCheckpoint = new SimpleCheckpointDTO();
        simpleCheckpoint.setId(getServerID());
        simpleCheckpoint.setRaceID(getRaceID());
        simpleCheckpoint.setTitle(getTitle());
        simpleCheckpoint.setDescription(getDescription());
        simpleCheckpoint.setLatitude(getLatitude());
        simpleCheckpoint.setLongitude(getLongitude());
        simpleCheckpoint.setAltitude(getAltitude());

        return simpleCheckpoint;
    }


}
