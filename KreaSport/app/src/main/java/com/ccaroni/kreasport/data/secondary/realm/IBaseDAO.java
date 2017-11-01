package com.ccaroni.kreasport.data.secondary.realm;

import com.ccaroni.kreasport.data.secondary.dto.IBaseDTO;

import io.realm.RealmModel;

/**
 * Created by Master on 01/11/2017.
 */

public interface IBaseDAO<T extends IBaseDTO> extends RealmModel {

    T toDTO();

}
