package com.ccaroni.kreasport.data.secondary.realm;

import com.ccaroni.kreasport.data.secondary.dto.IRaceRecordDTO;
import com.ccaroni.kreasport.data.secondary.realm.impl.RealmLocation;

import io.realm.RealmList;


/**
 * Created by Master on 01/11/2017.
 */

public interface IRaceRecordDAO<T extends IRaceRecordDTO> extends IBaseDAO<T> {

    String getServerID();

    void setServerID(String id);

    String getRaceId();

    void setRaceId(String raceId);

    String getUserId();

    void setUserId(String userId);

    long getTimeExpired();

    void setTimeExpired(long timeExpired);

    String getDateTime();

    void setDateTime(String dateTime);

    boolean isInProgress();

    void setInProgress(boolean inProgress);

    long getBaseTime();

    void setBaseTime(long baseTime);

    int getTargetCheckpointIndex();

    void setTargetCheckpointIndex(int targetCheckpointIndex);

    int getGeofenceProgression();

    void setGeofenceProgression(int geofenceProgression);

    boolean isSynced();

    void setSynced(boolean synced);

    boolean isToDelete();

    void setToDelete(boolean toDelete);

    RealmList<RealmLocation> getUserPath();

    void setUserPath(RealmList<RealmLocation> userPath);

    @Override
    T toDTO();
}
