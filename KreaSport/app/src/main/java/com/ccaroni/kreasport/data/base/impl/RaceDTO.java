package com.ccaroni.kreasport.data.base.impl;

import com.ccaroni.kreasport.data.base.AbstractCheckpointDTO;
import com.ccaroni.kreasport.data.base.AbstractRaceDTO;
import com.ccaroni.kreasport.data.dao.AbstractCheckpointDAO;
import com.ccaroni.kreasport.data.dao.AbstractRaceDAO;
import com.ccaroni.kreasport.data.dao.impl.RaceDAO;

import java.util.ArrayList;
import java.util.List;

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

        abstractRaceDAO.setCheckpoints(checkpointsToDTO());

        return abstractRaceDAO;
    }

    @Override
    protected List<AbstractCheckpointDAO<?>> checkpointsToDTO() {
        List<AbstractCheckpointDAO<?>> checkpointDAOS = new ArrayList<>();
        for (T checkpointDTO : getCheckpoints()) {
            checkpointDAOS.add(checkpointDTO.toDAO());
        }
        return checkpointDAOS;
    }

}
