package com.ccaroni.kreasport.data.base;

import com.ccaroni.kreasport.data.dao.BaseDAO;

/**
 * Created by Master on 01/11/2017.
 */

public interface BaseDTO<T extends BaseDAO> {

    T toDAO();

}
