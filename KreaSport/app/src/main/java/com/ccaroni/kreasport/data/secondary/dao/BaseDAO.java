package com.ccaroni.kreasport.data.secondary.dao;

import com.ccaroni.kreasport.data.secondary.dto.BaseDTO;

/**
 * Created by Master on 01/11/2017.
 */

public interface BaseDAO<T extends BaseDTO> {

    T toDTO();

}
