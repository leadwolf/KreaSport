package com.ccaroni.kreasport.data.dao.impl;

import com.ccaroni.kreasport.data.base.AbstractRaceDTO;
import com.ccaroni.kreasport.data.base.impl.RaceDTO;
import com.ccaroni.kreasport.data.dao.AbstractCheckpointDAO;
import com.ccaroni.kreasport.data.dao.AbstractRaceDAO;

/**
 * Created by Master on 01/11/2017.
 */

public class RaceDAO<T extends AbstractCheckpointDAO<?>> extends AbstractRaceDAO<T> {

    public RaceDAO() {
        super();
    }

    @Override
    public AbstractRaceDTO<?> toDTO() {
        AbstractRaceDTO raceDTO = new RaceDTO();
        raceDTO.setId(getServerID());
        raceDTO.setTitle(getTitle());
        raceDTO.setDescription(getDescription());
        raceDTO.setLatitude(getLatitude());
        raceDTO.setLongitude(getLongitude());
        raceDTO.setAltitude(getAltitude());

        raceDTO.setCheckpoints(getCheckpoints());

        return raceDTO;
    }

}
