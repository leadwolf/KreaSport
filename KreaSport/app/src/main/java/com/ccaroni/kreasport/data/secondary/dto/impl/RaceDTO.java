package com.ccaroni.kreasport.data.secondary.dto.impl;

import com.ccaroni.kreasport.data.secondary.dao.AbstractCheckpointDAO;
import com.ccaroni.kreasport.data.secondary.dao.AbstractRaceDAO;
import com.ccaroni.kreasport.data.secondary.dao.impl.RaceDAO;
import com.ccaroni.kreasport.data.secondary.dto.AbstractCheckpointDTO;
import com.ccaroni.kreasport.data.secondary.dto.AbstractRaceDTO;

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
