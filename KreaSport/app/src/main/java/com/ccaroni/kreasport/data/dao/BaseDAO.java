package com.ccaroni.kreasport.data.dao;

import com.ccaroni.kreasport.data.dto.BaseDTO;

/**
 * Created by Master on 01/11/2017.
 */

public interface BaseDAO<T extends BaseDTO> {

    T toDTO();

}
