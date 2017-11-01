package com.ccaroni.kreasport.data.dto.impl;

import com.ccaroni.kreasport.data.dto.AbstractCheckpointDTO;
import com.ccaroni.kreasport.data.dao.impl.SimpleCheckpointDAO;

/**
 * Created by Master on 01/11/2017.
 */

public class SimpleCheckpointDTO extends AbstractCheckpointDTO<SimpleCheckpointDAO> {

    public SimpleCheckpointDTO() {
        super();
    }

    @Override
    public SimpleCheckpointDAO toDAO() {
        SimpleCheckpointDAO simpleCheckpointDAO = new SimpleCheckpointDAO();
        simpleCheckpointDAO.setServerID(getId());
        simpleCheckpointDAO.setRaceID(getRaceID());
        simpleCheckpointDAO.setTitle(getTitle());
        simpleCheckpointDAO.setDescription(getDescription());
        simpleCheckpointDAO.setLatitude(getLatitude());
        simpleCheckpointDAO.setLongitude(getLongitude());
        simpleCheckpointDAO.setAltitude(getAltitude());

        return simpleCheckpointDAO;
    }

}
