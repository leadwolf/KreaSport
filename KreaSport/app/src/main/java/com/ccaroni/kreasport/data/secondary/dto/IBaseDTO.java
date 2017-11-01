package com.ccaroni.kreasport.data.secondary.dto;

import com.ccaroni.kreasport.data.secondary.realm.IBaseDAO;

/**
 * Created by Master on 01/11/2017.
 */

public interface IBaseDTO<T extends IBaseDAO> {

    T toDAO();

}
