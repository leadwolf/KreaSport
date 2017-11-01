package com.ccaroni.kreasport.data.secondary.realm;

import com.ccaroni.kreasport.data.legacy.realm.RealmLocation;
import com.ccaroni.kreasport.data.secondary.dto.IRaceRecordDTO;

import io.objectbox.annotation.Entity;
import io.realm.RealmList;

/**
 * Created by Master on 01/11/2017.
 */

@Entity
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

    public boolean isInProgress();

    public void setInProgress(boolean inProgress);

    public long getBaseTime();

    public void setBaseTime(long baseTime);

    public int getTargetCheckpointIndex();

    public void setTargetCheckpointIndex(int targetCheckpointIndex);

    public int getGeofenceProgression();

    public void setGeofenceProgression(int geofenceProgression);

    public boolean isSynced();

    public void setSynced(boolean synced);

    public boolean isToDelete();

    public void setToDelete(boolean toDelete);

    public RealmList<RealmLocation> getUserPath();

    public void setUserPath(RealmList<RealmLocation> userPath);

    @Override
    T toDTO();
}
