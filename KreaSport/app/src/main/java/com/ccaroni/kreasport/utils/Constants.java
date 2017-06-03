package com.ccaroni.kreasport.utils;

import com.ccaroni.kreasport.view.activities.AreaSelectionActivity;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.ccaroni.kreasport.data.dto.Race;

/**
 * Created by Master on 30/03/2017.
 */

public class Constants {

    public static final String GEOFENCE_RECEIVER_ID = "com.ccaroni.kreasport.geofence_receiver";

    /**
     * Minimum distance that the user needs to be from a checkpoint to trigger the geofence.
     */
    public static final float GEOFENCE_RADIUS_METERS = 20;
    /**
     * Minimum amount of time the user has to be in GEOFENCE_RADIUS_METERS to trigger the geofence.
     */
    public static final int GEOFENCE_LOITERING_DELAY = 2000; // in ms
    public static final long GEOFENCE_EXPIRATION_MILLISECONDS = 300000; // 5mn;

    /**
     * The normal update interval requested in ms.
     */
    public static final long GEOLOCATION_UPDATE_INTERVAL = 2500; // in ms

    /**
     * The smallest amount of time we force between updates in ms.
     */
    public static final long GEOLOCATION_UPDATE_FASTEST_INTERVAL = 1500; // in ms


    /**
     * Minimum distance that the user needs to be from the start of a race in m (meters).
     */
    public static final float MINIMUM_DISTANCE_TO_START_RACE = 20; // in m


    public static final int AUTO_ZOOM_LEVEL = 15;

    /**
     * The minimum the user has to be zoomed in to prevent downloading to large of an area.
     */
    public static final Integer DOWNLOAD_MIN_ZOOM = 11;

    /**
     * The maximum the user can be zoomed in to prevent the server from having to render the area for the download.
     */
    public static final Integer DOWNLOAD_MAX_ZOOM = 17;

    /**
     * Average size of one downloaded tile
     */
    public static final int TILE_KB_SIZE = 15;

    public static String getBaseString(String simpleName) {
        return "com.ccaroni.kreasport." + simpleName;
    }


    private static final String KEY_BASE = Constants.getBaseString(Constants.class.getSimpleName()) + "keys.";
    public static final String KEY_AREA_ID = KEY_BASE + "area_id";
}
