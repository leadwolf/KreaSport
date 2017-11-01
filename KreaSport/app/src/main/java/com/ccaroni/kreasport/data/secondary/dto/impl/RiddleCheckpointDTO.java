package com.ccaroni.kreasport.data.secondary.dto.impl;

import com.ccaroni.kreasport.data.secondary.dao.impl.RiddleCheckpointDAO;
import com.ccaroni.kreasport.data.secondary.dto.AbstractCheckpointDTO;
import com.ccaroni.kreasport.data.secondary.dto.AbstractRiddleDTO;

/**
 * Created by Master on 01/11/2017.
 */

public class RiddleCheckpointDTO extends AbstractCheckpointDTO<RiddleCheckpointDAO> {

    protected AbstractRiddleDTO riddle;

    public RiddleCheckpointDTO() {
        super();
        riddle = new RiddleDTO();
    }

    public AbstractRiddleDTO getRiddle() {
        return riddle;
    }

    public void setRiddle(AbstractRiddleDTO riddle) {
        this.riddle = riddle;
    }

    @Override
    public RiddleCheckpointDAO toDAO() {
        RiddleCheckpointDAO riddleCheckpointDAO = new RiddleCheckpointDAO();
        riddleCheckpointDAO.setServerID(getId());
        riddleCheckpointDAO.setRaceID(getRaceID());
        riddleCheckpointDAO.setTitle(getTitle());
        riddleCheckpointDAO.setDescription(getDescription());
        riddleCheckpointDAO.setLatitude(getLatitude());
        riddleCheckpointDAO.setLongitude(getLongitude());
        riddleCheckpointDAO.setAltitude(getAltitude());

        riddleCheckpointDAO.setRiddle(riddle.toDAO());

        return riddleCheckpointDAO;
    }
}
