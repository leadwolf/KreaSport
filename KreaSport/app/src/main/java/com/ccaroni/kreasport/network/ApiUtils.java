package com.ccaroni.kreasport.network;

/**
 * Created by Master on 23/04/2017.
 */

public class ApiUtils {

    private static final String BASE_URL = "http://kreasport.herokuapp.com/races/";
    private static final String DEBUG_URL = "http://10.0.2.2:8080/races/";

    public static RaceService getRaceService(boolean local) {
        if (local)
            return RetrofitClient.getClient(DEBUG_URL).create(RaceService.class);
        return RetrofitClient.getClient(BASE_URL).create(RaceService.class);
    }

    public static UserService getUserService(boolean local) {
        if (local)
            return RetrofitClient.getClient(DEBUG_URL).create(UserService.class);
        return RetrofitClient.getClient(BASE_URL).create(UserService.class);
    }

}
