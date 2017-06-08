package com.ccaroni.kreasport.data;

import android.app.Activity;
import android.util.Log;

import com.ccaroni.kreasport.data.dto.Race;
import com.ccaroni.kreasport.data.dto.RaceRecord;
import com.ccaroni.kreasport.data.realm.DownloadedArea;
import com.ccaroni.kreasport.data.realm.RealmRace;
import com.ccaroni.kreasport.data.realm.RealmRaceRecord;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by Master on 19/05/2017.
 */

public class RealmHelper {

    private static final String LOG = RealmHelper.class.getSimpleName();

    private static RealmHelper instance;

    private Realm realm;
    private List<Race> racesForOverlay;


    private RealmHelper() {

    }

    public static synchronized RealmHelper getInstance(Activity activity) {
        if (instance == null) {
            synchronized (RealmHelper.class) {
                if (instance == null) {
                    instance = new RealmHelper();
                    instance.init(activity);
                }
            }
        }
        return instance;
    }

    public void init(Activity activity) {
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

    /**
     * Finds the race matching the id.
     *
     * @param id the id to lookup
     * @return the race found or null
     */
    public RealmRace findRaceById(String id) {
        Log.d(LOG, "attempting to find race:" + id);

        return realm.where(RealmRace.class)
                .equalTo("id", id)
                .findFirst();
    }

    public RealmRace findSingleCurrentRace() {
        RealmRaceRecord currentRaceRecord = findCurrentRaceRecord();
        return realm.where(RealmRace.class)
                .equalTo("id", currentRaceRecord.getRaceId())
                .findFirst();
    }

    public RealmRaceRecord findCurrentRaceRecord() {
        return realm.where(RealmRaceRecord.class)
                .equalTo("inProgress", true)
                .findFirst();
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

    public RealmRaceRecord createRealmRaceRecord() {
        return realm.createObject(RealmRaceRecord.class);
    }

    public void deleteAllRaceRecords() {
        realm.delete(RealmRaceRecord.class);
    }

    /**
     * @param userId
     * @return records that have been started (because we pre load record objects in {@link com.ccaroni.kreasport.map.viewmodels.RaceVM} and that are <b>NOT</b> marked for deletion
     */
    public RealmResults<RealmRaceRecord> getMyRecords(String userId) {
        return realm.where(RealmRaceRecord.class)
                .equalTo("userId", userId)
                .equalTo("started", true)
                .equalTo("toDelete", false)
                .findAll();
    }

    public DownloadedArea createDownloadedArea() {
        return realm.createObject(DownloadedArea.class);
    }

    public <E extends RealmObject> E createRealmObject(Class<E> classz) {
        return realm.createObject(classz);
    }

    public DownloadedArea findDownloadedAreaById(String areaId) {
        return realm.where(DownloadedArea.class)
                .equalTo("id", areaId)
                .findFirst();
    }

    public RealmResults<DownloadedArea> findAllDownloadedAreas() {
        return realm.where(DownloadedArea.class)
                .findAll();
    }

    public void deleteDownloadedArea(DownloadedArea downloadedArea) {
        downloadedArea.deleteFromRealm();
    }


    public RealmRaceRecord findRecordById(String recordId) {
        return realm.where(RealmRaceRecord.class)
                .equalTo("id", recordId)
                .findFirst();
    }

    public RealmResults<RealmRaceRecord> getRecordsToDelete() {
        return realm.where(RealmRaceRecord.class)
                .equalTo("toDelete", true)
                .findAll();
    }

}
