package com.ccaroni.kreasport.map.viewmodels;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ccaroni.kreasport.BR;
import com.ccaroni.kreasport.data.RaceHelper;
import com.ccaroni.kreasport.data.dto.Checkpoint;
import com.ccaroni.kreasport.data.realm.RealmCheckpoint;
import com.ccaroni.kreasport.data.realm.RealmRace;
import com.ccaroni.kreasport.map.views.CustomOverlayItem;
import com.ccaroni.kreasport.utils.Constants;
import com.ccaroni.kreasport.utils.LocationUtilContract;

import org.osmdroid.views.overlay.ItemizedIconOverlay;

import io.realm.RealmResults;

/**
 * Created by Master on 19/05/2017.
 */

public class RaceVM extends BaseObservable {

    private transient static final String LOG = RaceVM.class.getSimpleName();

    private RealmRace currentRace;
    private RealmCheckpoint currentCheckpoint;

    private String title;
    private String description;

    /**
     * Whether this VM is currently in an active state
     */
    private boolean raceActive;
    private transient int passiveInfoVisibility;
    private transient int activeInfoVisbility;
    private int fabVisibility;


    private Activity activity;
    private LocationUtilContract mLocationUtils;

    public RaceVM(Activity activity, LocationUtilContract mLocationUtils) {
        this.activity = activity;
        this.mLocationUtils = mLocationUtils;

        // set this at the start because normally it has to be triggered by a change
        RealmResults<RealmRace> currentRaceResults = RaceHelper.getInstance(activity).findCurrentRace();
        if (currentRaceResults.size() != 0) {
            currentRace = currentRaceResults.get(0);
            currentCheckpoint = currentRace.getCurrentCheckpoint();
            setRaceActive(true);
        } else {
            setRaceActive(false);
        }
    }

    public boolean isRaceActive() {
        return raceActive;
    }


    /**
     * Then entry point for modifying anything related to the event of starting/stopping the race.
     * <br>Includes visibility and race attribute itself (to be saved with Realm).
     * @param newRaceState
     */
    public void setRaceActive(boolean newRaceState) {
        Log.d(LOG, newRaceState ? "set race active" : "set race INactive");


        changeVisibilitiesOnRaceState(newRaceState);

        if (this.raceActive) {
            currentRace.setInProgress(newRaceState);
        }

        notifyChange();
        this.raceActive = newRaceState;
    }

    private void changeVisibilitiesOnRaceState(boolean raceActive) {
        passiveInfoVisibility = raceActive ? View.GONE : View.VISIBLE;
        fabVisibility = raceActive ? View.GONE : View.VISIBLE;
        activeInfoVisbility = raceActive ? View.VISIBLE : View.GONE;
    }

    @Bindable
    public int getPassiveInfoVisibility() {
        return passiveInfoVisibility;
    }

    @Bindable
    public int getActiveInfoVisibility() {
        return activeInfoVisbility;
    }

    @Bindable
    public int getFabVisibility() {
        return fabVisibility;
    }

    public ItemizedIconOverlay.OnItemGestureListener<CustomOverlayItem> getIconGestureListener() {
        return new ItemizedIconOverlay.OnItemGestureListener<CustomOverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(final int index, final CustomOverlayItem item) {
                Log.d(LOG, "on tap, is primary: " + item.isPrimary());

                updateCurrent(item);

                return true;
            }

            @Override
            public boolean onItemLongPress(final int index, final CustomOverlayItem item) {
                return false;
            }
        };
    }

    /**
     * Switch to update on a {@link CustomOverlayItem} selection. Calls the appropriate method for updating title & description according to the selectedItem.
     * @param selectedItem
     */
    private void updateCurrent(CustomOverlayItem selectedItem) {
        if (raceActive) {
            updateFromActiveState(selectedItem);
        } else {
            updateFromInactiveState(selectedItem);
        }
    }

    /**
     * Called only when raceActive
     * @param selectedItem
     */
    private void updateFromActiveState(CustomOverlayItem selectedItem) {
        if (!raceActive) {
            throw new IllegalStateException("This method is only supposed to be called from a raceActive state");
        }
        if (selectedItem.isPrimary()) {
            Log.d(LOG, "selected the race marker of the ongoing race");
            setTitle(currentRace.getTitle());
            setDescription(currentRace.getDescription());
        } else {
            if (currentCheckpoint.getId().equals(selectedItem.getId())) {
                Log.d(LOG, "selected same race, same checkpoint");
            } else {
                Log.d(LOG, "selected same race, difference checkpoint");
                currentCheckpoint = currentRace.getCheckpointById(selectedItem.getId());
                setTitle(currentCheckpoint.getTitle());
                setDescription(currentCheckpoint.getDescription());
            }
        }
    }

    /**
     * Called only when !raceActive
     * @param selectedItem
     */
    private void updateFromInactiveState(CustomOverlayItem selectedItem) {
        if (raceActive) {
            throw new IllegalStateException("This method is only supposed to be called from a !raceActive state");
        }
        if (currentRace != null && currentRace.getId().equals(selectedItem.getRaceId())) {
            Log.d(LOG, "selected same race");
        } else {
            Log.d(LOG, "selected different race");

            RaceHelper.getInstance(activity).getAllRaces(false);

            currentRace = RaceHelper.getInstance(activity).findRaceById(selectedItem.getRaceId());
            setTitle(currentRace.getTitle());
            setDescription(currentRace.getDescription());
        }
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }

    @Bindable
    public String getProgression() {
        if (currentRace == null) {
            Log.d(LOG, "No race active to get progression from");
            return null;
        }
        return currentRace.getProgressionAsString();
    }

    /**
     * Call to stop the current race. Used by passiveBottomSheet.
     */
    public void onStartClicked() {
        Log.d(LOG, "start clicked");
        if (raceActive) {
            throw new IllegalStateException("A race is already active");
        } else if (currentRace == null) {
            throw new IllegalStateException("No race is currently selected");
        }

        Location lastLocation = mLocationUtils.getLastKnownLocation();
        Location raceLocation = currentRace.getLocation();

        float distance = lastLocation.distanceTo(raceLocation);

        if (distance > Constants.MINIMUM_DISTANCE_TO_START_RACE) {
            Log.d(LOG, "User was " + distance + "m away from start. Too far by " + (distance - Constants.MINIMUM_DISTANCE_TO_START_RACE) + "m");
            Toast.makeText(activity, "You are too far to start this race", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(LOG, "User was " + distance + "m away from start. Inside by " + (Constants.MINIMUM_DISTANCE_TO_START_RACE - distance) + "m");
            Toast.makeText(activity, "Started!", Toast.LENGTH_SHORT).show();

            setRaceActive(true);
        }

    }

    /**
     * Call to stop the current race. Used by activeBottomSheet.
     */
    public void onStopClicked() {
        Log.d(LOG, "stop clicked");
        if (!raceActive) {
            throw new IllegalStateException("A race is already active");
        }

        setRaceActive(false);
    }

    public RealmCheckpoint getActiveCheckpoint() {
        return currentCheckpoint;
    }
}
