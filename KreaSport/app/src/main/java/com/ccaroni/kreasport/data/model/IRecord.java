package com.ccaroni.kreasport.data.model;

/**
 * Created by Master on 09/02/2018.
 */

public interface IRecord {
    long getId();

    String getRaceId();

    String getUserId();

    long getTimeExpired();

    String getDateTime();
}
