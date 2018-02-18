package com.ccaroni.kreasport.data;

import android.support.constraint.solver.widgets.Rectangle;

import com.ccaroni.kreasport.data.local.Location;
import com.ccaroni.kreasport.data.remote.Checkpoint;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.relation.ToMany;

/**
 * Created by Master on 10/02/2018.
 */

public class Converter {

    public static Checkpoint daoCheckpointToDTO(final com.ccaroni.kreasport.data.local.Checkpoint checkpoint) {
        Checkpoint dtoCheckpoint = new Checkpoint();
        dtoCheckpoint.setId(checkpoint.getId());
        dtoCheckpoint.setTitle(checkpoint.getTitle());
        dtoCheckpoint.setDescription(checkpoint.getDescription());
        dtoCheckpoint.setLatitude(checkpoint.getLatitude());
        dtoCheckpoint.setLongitude(checkpoint.getLongitude());
        return dtoCheckpoint;
    }

    public static com.ccaroni.kreasport.data.local.Checkpoint dtoCheckpointToDAO(Checkpoint dtoCheckpoint) {
        com.ccaroni.kreasport.data.local.Checkpoint checkpoint = new com.ccaroni.kreasport.data.local.Checkpoint();
        checkpoint.setId(dtoCheckpoint.getId());
        checkpoint.setTitle(dtoCheckpoint.getTitle());
        checkpoint.setDescription(dtoCheckpoint.getDescription());
        checkpoint.setLatitude(dtoCheckpoint.getLatitude());
        checkpoint.setLongitude(dtoCheckpoint.getLongitude());
        return checkpoint;
    }

    public static List<Checkpoint> daoCheckpointListToDTO(List<com.ccaroni.kreasport.data.local.Checkpoint> checkpointList) {
        List<Checkpoint> dtoCheckpointsList = new ArrayList<>();
        for (com.ccaroni.kreasport.data.local.Checkpoint checkpoint : checkpointList) {
            dtoCheckpointsList.add(daoCheckpointToDTO(checkpoint));
        }
        return dtoCheckpointsList;
    }

    public static List<com.ccaroni.kreasport.data.local.Checkpoint> dtoCheckpointListToDAO(List<Checkpoint> dtoCheckpointList) {
        List<com.ccaroni.kreasport.data.local.Checkpoint> checkpointList = new ArrayList<>();
        for (Checkpoint dtoCheckpoint : dtoCheckpointList) {
            checkpointList.add(dtoCheckpointToDAO(dtoCheckpoint));
        }
        return checkpointList;
    }

    public static Rectangle daoRectangleToDTO(com.ccaroni.kreasport.data.local.Rectangle rectangle) {
        android.support.constraint.solver.widgets.Rectangle rect = new android.support.constraint.solver.widgets.Rectangle();
        rect.setBounds(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
        return rect;
    }

    public static android.location.Location daoLocationToDTO(Location location) {
        android.location.Location dtoLocation = new android.location.Location("realm_location");
        location.setLongitude(location.getLongitude());
        location.setLatitude(location.getLatitude());
        location.setAltitude(location.getAltitude());
        return dtoLocation;
    }

    public static Location dtoLocationToDAO(android.location.Location dtoLocation) {
        Location location = new Location();
        location.setLongitude(dtoLocation.getLongitude());
        location.setLatitude(dtoLocation.getLatitude());
        location.setAltitude(dtoLocation.getAltitude());
        return location;
    }

    public static List<android.location.Location> daoLocationListToDTO(List<Location> path) {
        List<android.location.Location> dtoLocationList = new ArrayList<>();
        for (Location location : path) {
            dtoLocationList.add(daoLocationToDTO(location));
        }
        return dtoLocationList;
    }

    public static List<Location> dtoLocationListToDAO(List<android.location.Location> dtoPath) {
        List<Location> locationList = new ArrayList<>();
        for (android.location.Location dtoLocation : dtoPath) {
            locationList.add(dtoLocationToDAO(dtoLocation));
        }
        return locationList;
    }
}
