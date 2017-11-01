package com.ccaroni.kreasport.data.secondary.dto;

import com.ccaroni.kreasport.data.secondary.realm.IRaceRecordDAO;

/**
 * Created by Master on 01/11/2017.
 */

public interface IRaceRecordDTO<T extends IRaceRecordDAO> extends IBaseDTO<T> {

    String getId();

    void setId(String id);

    String getRaceId();

    void setRaceId(String raceId);

    String getUserId();

    void setUserId(String userId);

    long getTimeExpired();

    void setTimeExpired(long timeExpired);

    String getDateTime();

    void setDateTime(String dateTime);

    @Override
    T toDAO();
}
