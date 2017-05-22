package com.ccaroni.kreasport.data;

import android.app.Activity;
import android.util.Log;

import com.ccaroni.kreasport.data.dto.Race;
import com.ccaroni.kreasport.data.realm.RealmRace;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by Master on 19/05/2017.
 */

public class RaceHelper {

    private static final String LOG = RaceHelper.class.getSimpleName();

    private static RaceHelper instance;

    private Realm realm;
    private List<Race> racesForOverlay;


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
                .deleteRealmIfMigrationNeeded()
                .build()
        );

        // TODO DONT delete if migration
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

    public RealmResults<RealmRace> getAllRaces(boolean debug) {
        if (debug)
            Log.d(LOG, "getting all races");

        RealmResults<RealmRace> results = realm.where(RealmRace.class)
                .findAll();

        if (debug) {
            for (RealmRace realmRace : results) {
                Log.d(LOG, "found race: " + realmRace.getId());
            }
        }

        Log.d(LOG, "found " + results.size() + " races");

        return results;
    }

    public RealmRace findRaceById(String id) {
        Log.d(LOG, "attempting to find race:" + id);

        return realm.where(RealmRace.class)
                .equalTo("id", id)
                .findFirst();
    }

    public RealmResults<RealmRace> findCurrentRace() {
        return realm.where(RealmRace.class)
                .equalTo("inProgress", true)
                .findAll();
    }

    public RealmResults<RealmRace> getAllOrCurrentRace() {
        RealmResults<RealmRace> currentRealmResults = findCurrentRace();
        if (currentRealmResults.size() == 0) {
            return getAllRaces(false);
        } else {
            return currentRealmResults;
        }
    }

    public void deleteAllRaces() {
        realm.delete(RealmRace.class);
    }

    public void beginTransaction() {
        realm.beginTransaction();
    }

    public void commitTransaction() {
        realm.commitTransaction();
    }
}
