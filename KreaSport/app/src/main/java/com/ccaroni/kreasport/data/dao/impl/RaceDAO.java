package com.ccaroni.kreasport.data.dao.impl;

import com.ccaroni.kreasport.data.dao.AbstractCheckpointDAO;
import com.ccaroni.kreasport.data.dao.AbstractRaceDAO;

/**
 * Created by Master on 01/11/2017.
 */

public class RaceDAO<T extends AbstractCheckpointDAO> extends AbstractRaceDAO<T> {

    public RaceDAO() {
        super();
    }
}
