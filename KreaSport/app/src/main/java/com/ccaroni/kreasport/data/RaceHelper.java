package com.ccaroni.kreasport.data;

import android.app.Activity;

import com.ccaroni.kreasport.map.models.Race;

/**
 * Created by Master on 19/05/2017.
 */

public class RaceHelper {

    private final static String KEY_ALL_RACES = "com.ccaroni.kreasport." + RaceHelper.class.getSimpleName().toLowerCase() + ".keys." + "all_races";

    private static RaceHelper instance;


    private RaceHelper() {

    }

    public static synchronized RaceHelper getInstance(Activity activity) {
        if (instance == null) {
            synchronized (RaceHelper.class) {
                if (instance == null) {
                    instance = new RaceHelper();
                }
            }
        }
        return instance;
    }



    public static void saveRace(Race race) {

    }


}
