package com.ccaroni.kreasport.data.base.impl;

import com.ccaroni.kreasport.data.base.AbstractCheckpointDTO;
import com.ccaroni.kreasport.data.base.AbstractRaceDTO;
import com.ccaroni.kreasport.data.dao.AbstractRaceDAO;
import com.ccaroni.kreasport.data.dao.impl.RaceDAO;

/**
 * Created by Master on 01/11/2017.
 */

public class RaceDTO<T extends AbstractCheckpointDTO<?>> extends AbstractRaceDTO<T> {

    public RaceDTO() {
        super();
    }

    @Override
    public AbstractRaceDAO<?> toDAO() {
        AbstractRaceDAO abstractRaceDAO = new RaceDAO();
        abstractRaceDAO.setServerID(getId());
        abstractRaceDAO.setTitle(getTitle());
        abstractRaceDAO.setDescription(getDescription());
        abstractRaceDAO.setLatitude(getLatitude());
        abstractRaceDAO.setLongitude(getLongitude());
        abstractRaceDAO.setAltitude(getAltitude());

        abstractRaceDAO.setCheckpoints(getCheckpoints());

        return abstractRaceDAO;
    }

}
