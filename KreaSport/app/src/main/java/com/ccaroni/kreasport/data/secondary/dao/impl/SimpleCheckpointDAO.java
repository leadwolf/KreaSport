package com.ccaroni.kreasport.data.secondary.dao.impl;

import com.ccaroni.kreasport.data.secondary.dao.AbstractCheckpointDAO;
import com.ccaroni.kreasport.data.secondary.dto.impl.SimpleCheckpointDTO;

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
