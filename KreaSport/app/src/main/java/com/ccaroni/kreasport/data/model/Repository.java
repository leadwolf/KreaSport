package com.ccaroni.kreasport.data.model;

/**
 * Created by Master on 16/02/2018.
 */

public interface Repository<C extends Data> {

    C getById(long id);

    long save(C c);

    void delete(C c);

    void deleteById(long id);

}
