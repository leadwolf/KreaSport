package com.ccaroni.kreasport.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import com.ccaroni.kreasport.map.MapState;


public class PreferenceManager {

    private static final String FILE_PASS_RACES = "kreasport.preference_manager.file.pass_races";

    private static final String KEY_RACE_VM = "kreasport.prefrence_manager.keys.race_vm";
    private static final String KEY_MAP_STATE = "kreasport.preference_manager.keys.map_state";

    private SharedPreferences prefs;
    private Context context;

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public PreferenceManager(Context context, String prefName) {
        this.context = context;
        prefs = this.context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        prefs.edit()
                .putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime)
                .apply();
    }

    public boolean isFirstTimeLaunch() {
        return prefs.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void saveMapState(MapState mapState) {
        String mapStateJSON = new Gson().toJson(mapState, MapState.class);
        prefs.edit()
                .putString(KEY_MAP_STATE, mapStateJSON)
                .apply();
    }

    public MapState getMapState() {
        String mapStateVMJson = prefs.getString(KEY_MAP_STATE, "");
        return new Gson().fromJson(mapStateVMJson, MapState.class);
    }
}
