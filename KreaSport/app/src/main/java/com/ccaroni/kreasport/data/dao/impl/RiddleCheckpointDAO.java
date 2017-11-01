package com.ccaroni.kreasport.data.dao.impl;

import com.ccaroni.kreasport.data.base.impl.RiddleCheckpointDTO;
import com.ccaroni.kreasport.data.dao.AbstractCheckpointDAO;
import com.ccaroni.kreasport.data.dao.AbstractRiddleDAO;

/**
 * Created by Master on 01/11/2017.
 */

public class RiddleCheckpointDAO extends AbstractCheckpointDAO<RiddleCheckpointDTO> {

    protected AbstractRiddleDAO riddle;

    public RiddleCheckpointDAO() {
        super();
        riddle = new RiddleDAO();
    }

    public AbstractRiddleDAO getRiddle() {
        return riddle;
    }

    public void setRiddle(AbstractRiddleDAO riddle) {
        this.riddle = riddle;
    }

    @Override
    public RiddleCheckpointDTO toDTO() {
        RiddleCheckpointDTO riddleCheckpointDTO = new RiddleCheckpointDTO();
        riddleCheckpointDTO.setId(getServerID());
        riddleCheckpointDTO.setRaceID(getRaceID());
        riddleCheckpointDTO.setTitle(getTitle());
        riddleCheckpointDTO.setDescription(getDescription());
        riddleCheckpointDTO.setLatitude(getLatitude());
        riddleCheckpointDTO.setLongitude(getLongitude());
        riddleCheckpointDTO.setAltitude(getAltitude());

        riddleCheckpointDTO.setRiddle(riddle.toDTO());

        return riddleCheckpointDTO;
    }
}
