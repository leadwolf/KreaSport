package com.ccaroni.kreasport.data.secondary.dto.impl;

import com.ccaroni.kreasport.data.secondary.dao.impl.RaceRecordDAO;
import com.ccaroni.kreasport.data.secondary.dto.AbstractRaceRecordDTO;

/**
 * Created by Master on 01/11/2017.
 */

public class RaceRecordDTO extends AbstractRaceRecordDTO<RaceRecordDAO> {

    public RaceRecordDTO() {
        super();
    }

    @Override
    public RaceRecordDAO toDAO() {
        RaceRecordDAO raceRecordDAO = new RaceRecordDAO();
        raceRecordDAO.setServerID(getId());
        raceRecordDAO.setUserId(getUserId());
        raceRecordDAO.setRaceId(getRaceId());
        raceRecordDAO.setDateTime(getDateTime());
        raceRecordDAO.setTimeExpired(getTimeExpired());

        return raceRecordDAO;
    }
}
