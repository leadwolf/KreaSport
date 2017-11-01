package com.ccaroni.kreasport.data.secondary.dao.impl;

import com.ccaroni.kreasport.data.secondary.dao.AbstractCheckpointDAO;
import com.ccaroni.kreasport.data.secondary.dao.AbstractRaceDAO;
import com.ccaroni.kreasport.data.secondary.dto.AbstractCheckpointDTO;
import com.ccaroni.kreasport.data.secondary.dto.AbstractRaceDTO;
import com.ccaroni.kreasport.data.secondary.dto.impl.RaceDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Master on 01/11/2017.
 */

public class RaceDAO<T extends AbstractCheckpointDAO<AbstractCheckpointDTO>> extends AbstractRaceDAO<T> {

    public RaceDAO() {
        super();
    }

    @Override
    public AbstractRaceDTO<AbstractCheckpointDTO<?>> toDTO() {
        AbstractRaceDTO<AbstractCheckpointDTO<?>> raceDTO = new RaceDTO<>();
        raceDTO.setId(getServerID());
        raceDTO.setTitle(getTitle());
        raceDTO.setDescription(getDescription());
        raceDTO.setLatitude(getLatitude());
        raceDTO.setLongitude(getLongitude());
        raceDTO.setAltitude(getAltitude());

        raceDTO.setCheckpoints(checkpointsToDTO());

        return raceDTO;
    }

    @Override
    protected List<AbstractCheckpointDTO<?>> checkpointsToDTO() {
        List<AbstractCheckpointDTO<?>> checkpointDTOS = new ArrayList<>();
        for (T checkpointDAO : getCheckpoints()) {
            checkpointDTOS.add(checkpointDAO.toDTO());
        }
        return checkpointDTOS;
    }

}
