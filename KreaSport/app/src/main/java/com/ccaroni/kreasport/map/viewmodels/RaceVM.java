package com.ccaroni.kreasport.map.viewmodels;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;

import com.ccaroni.kreasport.BR;
import com.ccaroni.kreasport.data.RaceHelper;
import com.ccaroni.kreasport.data.dto.Checkpoint;
import com.ccaroni.kreasport.data.realm.RealmCheckpoint;
import com.ccaroni.kreasport.data.realm.RealmRace;
import com.ccaroni.kreasport.map.views.CustomOverlayItem;

import org.osmdroid.views.overlay.ItemizedIconOverlay;

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
    private Activity activity;

    public RaceVM(Activity activity) {
        this.activity = activity;
        raceActive = RaceHelper.getInstance(activity).findCurrentRace().size() != 0;
    }

    public boolean isRaceActive() {
        return raceActive;
    }

    public void setRaceActive(boolean raceActive) {
        this.raceActive = raceActive;
        passiveInfoVisibility = raceActive ? View.GONE : View.VISIBLE;
        activeInfoVisbility = raceActive ? View.INVISIBLE : View.GONE;
    }

    @Bindable
    public int getPassiveInfoVisibility() {
        return passiveInfoVisibility;
    }

    @Bindable
    public int getActiveInfoVisibility() {
        return activeInfoVisbility;
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
     * Switch to update call appropriate method for updating title & description according to the selectedItem.
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

            RaceHelper.getInstance(activity).getAllRaces(true);

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

    public void onStartClicked() {
        Log.d(LOG, "start clicked");
    }

    public RealmCheckpoint getActiveCheckpoint() {
        return currentCheckpoint;
    }
}
