package com.ccaroni.kreasport.data.secondary.dto;

import com.ccaroni.kreasport.data.secondary.realm.IBaseItemDAO;

/**
 * Created by Master on 01/11/2017.
 */

public interface IBaseItemDTO<T extends IBaseItemDAO> extends IBaseDTO<T> {

    String getId();

    void setId(String id);

    String getTitle();

    void setTitle(String title);

    String getDescription();

    void setDescription(String description);

    Double getLatitude();

    void setLatitude(Double latitude);

    Double getLongitude();

    void setLongitude(Double longitude);

    Double getAltitude();

    void setAltitude(Double altitude);

    @Override
    T toDAO();
}
