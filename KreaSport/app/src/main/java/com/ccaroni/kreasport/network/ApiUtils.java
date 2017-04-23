package com.ccaroni.kreasport.network;

/**
 * Created by Master on 23/04/2017.
 */

public class ApiUtils {

    public static final String BASE_URL = "http://kreasport.herokuapp.com/races/";

    public static RaceService getRaceService() {
        return RetrofitClient.getClient(BASE_URL).create(RaceService.class);
    }

}
