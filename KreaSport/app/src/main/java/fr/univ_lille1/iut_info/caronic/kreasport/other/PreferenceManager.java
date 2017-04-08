package fr.univ_lille1.iut_info.caronic.kreasport.other;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import fr.univ_lille1.iut_info.caronic.kreasport.map.viewmodels.MapVM;
import fr.univ_lille1.iut_info.caronic.kreasport.map.viewmodels.RaceVM;


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

    /**
     * Saves raceVM to {@link SharedPreferences} using the file {@link #FILE_PASS_RACES}.
     * @param raceVM
     */
    public void saveRaceVM(RaceVM raceVM) {
        SharedPreferences racePrefs = context.getSharedPreferences(FILE_PASS_RACES, Context.MODE_PRIVATE);
        String raceVMJSON = new Gson().toJson(raceVM, RaceVM.class);
        racePrefs.edit()
                .putString(KEY_RACE_VM, raceVMJSON)
                .apply();
    }

    /**
     * Gets the raceVM stored in {@link SharedPreferences} from the file {@link #FILE_PASS_RACES}.
     * @return
     */
    public RaceVM getRaceVM() {
        SharedPreferences racePrefs = context.getSharedPreferences(FILE_PASS_RACES, Context.MODE_PRIVATE);
        String raceVMJson = racePrefs.getString(KEY_RACE_VM, "");
        if (raceVMJson.equals("")) {
            return new RaceVM();
        } else {
            return new Gson().fromJson(raceVMJson, RaceVM.class);
        }
    }

    public void saveMapState(MapVM mapVM) {
        String mapStateJSON = new Gson().toJson(mapVM, MapVM.class);
        prefs.edit()
                .putString(KEY_MAP_STATE, mapStateJSON)
                .apply();
    }

    public MapVM getMapState() {
        String mapStateVMJson = prefs.getString(KEY_MAP_STATE, "");
        return new Gson().fromJson(mapStateVMJson, MapVM.class);
    }
}
