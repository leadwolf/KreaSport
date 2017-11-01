package com.ccaroni.kreasport.data.secondary.realm;

import com.ccaroni.kreasport.data.secondary.dto.IBaseItemDTO;

import io.objectbox.annotation.Entity;

/**
 * Created by Master on 01/11/2017.
 */

@Entity
public interface IBaseItemDAO<T extends IBaseItemDTO> extends IBaseDAO<T> {

    String getServerID();

    void setServerID(String id);

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
    T toDTO();
}
