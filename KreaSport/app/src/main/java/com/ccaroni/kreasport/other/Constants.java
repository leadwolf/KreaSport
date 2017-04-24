package com.ccaroni.kreasport.other;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.ccaroni.kreasport.map.models.Race;

/**
 * Created by Master on 30/03/2017.
 */

public class Constants {

    private static final String baseIP = "http://51.255.131.189/";

    public static final String publicRacesURL = baseIP + "v1/parcours";
    public static final String privateRaceURL = publicRacesURL + "%s";


    public static final String KEY_SAVED_RACES = "kreasport.global.keys.saved_races";
    public static final Type savedRaceLisType = new TypeToken<ArrayList<Race>>(){}.getType();

    public static final float GEOFENCE_RADIUS_METERS = 20;
    public static final long GEOFENCE_EXPIRATION_MILLISECONDS = 300000; // 5mn;
    public static final long GEOLOCATION_UPDATE_INTERVAL = 25000;
    public static final long GEOLOCATION_UPDATE_FASTEST_INTERVAL = 15000;
}
