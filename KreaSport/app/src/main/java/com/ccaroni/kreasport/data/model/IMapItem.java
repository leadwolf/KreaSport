package com.ccaroni.kreasport.data.model;

/**
 * Created by Master on 09/02/2018.
 */

public interface IMapItem extends Data {

    long getId();

    String getTitle();

    String getDescription();

    double getLatitude();

    double getLongitude();
}
