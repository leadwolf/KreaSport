package com.ccaroni.kreasport.data;

import android.support.constraint.solver.widgets.Rectangle;

import com.ccaroni.kreasport.data.remote.Checkpoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Master on 10/02/2018.
 */

public class Converter {

    public static Checkpoint daoCheckpointToDTO(final com.ccaroni.kreasport.data.local.domain.Checkpoint checkpoint) {
        Checkpoint dtoCheckpoint = new Checkpoint();
        dtoCheckpoint.setId(checkpoint.getId());
        dtoCheckpoint.setTitle(checkpoint.getTitle());
        dtoCheckpoint.setDescription(checkpoint.getDescription());
        dtoCheckpoint.setLatitude(checkpoint.getLatitude());
        dtoCheckpoint.setLongitude(checkpoint.getLongitude());
        return dtoCheckpoint;
    }

    public static com.ccaroni.kreasport.data.local.domain.Checkpoint dtoCheckpointToDAO(Checkpoint dtoCheckpoint) {
        com.ccaroni.kreasport.data.local.domain.Checkpoint checkpoint = new com.ccaroni.kreasport.data.local.domain.Checkpoint();
        checkpoint.setId(dtoCheckpoint.getId());
        checkpoint.setTitle(dtoCheckpoint.getTitle());
        checkpoint.setDescription(dtoCheckpoint.getDescription());
        checkpoint.setLatitude(dtoCheckpoint.getLatitude());
        checkpoint.setLongitude(dtoCheckpoint.getLongitude());
        return checkpoint;
    }

    public static List<Checkpoint> daoCheckpointListToDTO(List<com.ccaroni.kreasport.data.local.domain.Checkpoint> checkpointList) {
        List<Checkpoint> dtoCheckpointsList = new ArrayList<>();
        for (com.ccaroni.kreasport.data.local.domain.Checkpoint checkpoint : checkpointList) {
            dtoCheckpointsList.add(daoCheckpointToDTO(checkpoint));
        }
        return dtoCheckpointsList;
    }

    public static List<com.ccaroni.kreasport.data.local.domain.Checkpoint> dtoCheckpointListToDAO(List<Checkpoint> dtoCheckpointList) {
        List<com.ccaroni.kreasport.data.local.domain.Checkpoint> checkpointList = new ArrayList<>();
        for (Checkpoint dtoCheckpoint : dtoCheckpointList) {
            checkpointList.add(dtoCheckpointToDAO(dtoCheckpoint));
        }
        return checkpointList;
    }

    public static Rectangle daoRectangleToDTO(com.ccaroni.kreasport.data.local.domain.Rectangle rectangle) {
        android.support.constraint.solver.widgets.Rectangle rect = new android.support.constraint.solver.widgets.Rectangle();
        rect.setBounds(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
        return rect;
    }

}
