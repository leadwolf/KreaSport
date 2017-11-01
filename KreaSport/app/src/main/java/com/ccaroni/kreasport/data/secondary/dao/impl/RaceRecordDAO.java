package com.ccaroni.kreasport.data.secondary.dao.impl;

import com.ccaroni.kreasport.data.secondary.dao.AbstractRaceRecordDAO;
import com.ccaroni.kreasport.data.secondary.dto.impl.RaceRecordDTO;

/**
 * Created by Master on 01/11/2017.
 */

public class RaceRecordDAO extends AbstractRaceRecordDAO<RaceRecordDTO> {

    public RaceRecordDAO() {
        super();
    }

    @Override
    public RaceRecordDTO toDTO() {
        RaceRecordDTO raceRecordDTO = new RaceRecordDTO();
        raceRecordDTO.setId(getServerID());
        raceRecordDTO.setUserId(getUserId());
        raceRecordDTO.setRaceId(getRaceId());
        raceRecordDTO.setDateTime(getDateTime());
        raceRecordDTO.setTimeExpired(getTimeExpired());

        return raceRecordDTO;
    }
}
