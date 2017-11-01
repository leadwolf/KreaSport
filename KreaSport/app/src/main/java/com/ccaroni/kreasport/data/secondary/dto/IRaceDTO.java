package com.ccaroni.kreasport.data.secondary.dto;


import com.ccaroni.kreasport.data.secondary.realm.ICheckpointDAO;
import com.ccaroni.kreasport.data.secondary.realm.IRaceDAO;

import java.util.List;

/**
 * Created by Master on 01/11/2017.
 */

public interface IRaceDTO<T extends ICheckpointDTO<?>> extends IBaseItemDTO<IRaceDAO<?>> {

    List<T> getCheckpoints();

    void setCheckpoints(List<T> checkpoints);

    @Override
    IRaceDAO<?> toDAO();

    List<ICheckpointDAO<?>> checkpointsToDTO();
}
