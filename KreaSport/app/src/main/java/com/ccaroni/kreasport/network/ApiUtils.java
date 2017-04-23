package com.ccaroni.kreasport.network;

/**
 * Created by Master on 23/04/2017.
 */

public class ApiUtils {

    public static final String BASE_URL = "http://kreasport.herokuapp.com/races/";
    public static final String DEBUG_URL = "http://10.0.2.2:8080/races/";

    public static RaceService getRaceService(boolean debug) {
        if (debug)
            return RetrofitClient.getClient(DEBUG_URL).create(RaceService.class);
        return RetrofitClient.getClient(BASE_URL).create(RaceService.class);
    }

}
