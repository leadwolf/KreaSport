package com.ccaroni.kreasport.data.secondary.dto;

import com.ccaroni.kreasport.data.secondary.dao.BaseDAO;

/**
 * Created by Master on 01/11/2017.
 */

public interface BaseDTO<T extends BaseDAO> {

    T toDAO();

}
