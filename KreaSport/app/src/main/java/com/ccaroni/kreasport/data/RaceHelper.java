package com.ccaroni.kreasport.data;

import android.app.Activity;

import com.ccaroni.kreasport.data.dto.Race;
import com.ccaroni.kreasport.data.realm.RealmRace;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Master on 19/05/2017.
 */

public class RaceHelper {

    private final static String KEY_ALL_RACES = "com.ccaroni.kreasport." + RaceHelper.class.getSimpleName().toLowerCase() + ".keys." + "all_races";

    private static RaceHelper instance;

    private Realm realm;


    private RaceHelper() {

    }

    public static synchronized RaceHelper getInstance(Activity activity) {
        if (instance == null) {
            synchronized (RaceHelper.class) {
                if (instance == null) {
                    instance = new RaceHelper();
                    instance.init(activity);
                }
            }
        }
        return instance;
    }

    private void init(Activity activity) {
        Realm.init(activity);

        realm = Realm.getInstance(new RealmConfiguration.Builder()
                .name("kreasport-db")
                .build()
        );
    }


    public RealmRace saveRace(Race race) {
        realm.beginTransaction();

        RealmRace realmRace = realm.copyToRealmOrUpdate(race.toRealmRace());

        realm.commitTransaction();

        return realmRace;
    }

    public List<RealmRace> saveRaceList(List<Race> raceList) {
        realm.beginTransaction();

        List<RealmRace> realmRaceList = realm.copyToRealmOrUpdate(Race.toRealmList(raceList));

        realm.commitTransaction();

        return realmRaceList;
    }

    public RealmRace findRaceById(String id) {
        RealmResults<RealmRace> realmResults = realm.where(RealmRace.class)
                .equalTo("id", id)
                .findAll();
        if (realmResults.size() == 0) {
            return null;
        }
        return realmResults.get(0);
    }


}
