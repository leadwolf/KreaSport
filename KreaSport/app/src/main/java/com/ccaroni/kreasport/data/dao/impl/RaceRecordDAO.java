package com.ccaroni.kreasport.data.dao.impl;

import com.ccaroni.kreasport.data.dto.impl.RaceRecordDTO;
import com.ccaroni.kreasport.data.dao.AbstractRaceRecordDAO;

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
