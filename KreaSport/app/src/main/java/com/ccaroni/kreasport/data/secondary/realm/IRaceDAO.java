package com.ccaroni.kreasport.data.secondary.realm;


import com.ccaroni.kreasport.data.secondary.dto.ICheckpointDTO;
import com.ccaroni.kreasport.data.secondary.dto.IRaceDTO;

import java.util.List;

import io.objectbox.annotation.Entity;
import io.realm.RealmList;

/**
 * Created by Master on 01/11/2017.
 */

@Entity
public interface IRaceDAO<C extends ICheckpointDAO<ICheckpointDTO>> extends IBaseItemDAO<IRaceDTO<ICheckpointDTO<?>>> {

    RealmList<C> getCheckpoints();

    void setCheckpoints(RealmList<C> checkpoints);

    @Override
    IRaceDTO<ICheckpointDTO<?>> toDTO();

    List<ICheckpointDTO<?>> checkpointsToDTO();
}
