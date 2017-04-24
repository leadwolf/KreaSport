package com.ccaroni.kreasport.map.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;

import org.osmdroid.views.overlay.ItemizedIconOverlay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.ccaroni.kreasport.BR;
import com.ccaroni.kreasport.map.views.CustomOverlayItem;
import com.ccaroni.kreasport.map.models.Checkpoint;
import com.ccaroni.kreasport.map.models.Race;

/**
 * Created by Master on 06/04/2017.
 * ViewModel for the races. Has a list of {@link Race} as atrtibute and is used by the views to represent that data.
 */
public class RaceVM extends BaseObservable {

    private transient static final String LOG = RaceVM.class.getSimpleName();

    /**
     * The race model to manipulate
     */
    private List<Race> races;

    /**
     * The checkpoints from the race model, simplifies chained gets.
     * Transient because we just load all the race data into this VM anyways.
     */
    private transient List<Checkpoint> checkpoints;

    /**
     * The race selected either from a passive or active viewpoint.
     */
    private int currentRaceIndex;

    /**
     * The current TARGET checkpoint for the currentRaceIndex.
     */
    private int currentCheckpointIndex;

    /**
     * Whether this VM is currently in an active state
     */
    private boolean raceActive;

    private transient int passiveInfoVisibility;
    private transient int activeInfoVisbility;

    /**
     * Empty constructor because either nothing is to be loaded or everything is to be restored from deserializing.
     */
    public RaceVM() {
        currentRaceIndex = -1;
        currentCheckpointIndex = -1;
        raceActive = false;
    }

    /**
     * @return the title for the current race if a race is not active, or the checkpoints description if active
     */
    @Bindable
    public String getTitle() {
        if (raceActive) {
            if (checkpoints != null && currentCheckpointIndex < checkpoints.size())
                return checkpoints.get(currentCheckpointIndex).getTitle();
            else
                return "No title available";
        } else {
            if (races != null && currentRaceIndex >= 0 && currentRaceIndex <= races.size())
                return races.get(currentRaceIndex).getTitle();
            else
                return "No title available";
        }
    }

    /**
     * @return the description for the current race if a race is not active, or the checkpoints description if active
     */
    @Bindable
    public String getDescription() {
        Log.d(LOG, "get desc");
        if (raceActive) {
            if (checkpoints != null && currentCheckpointIndex < checkpoints.size())
                return checkpoints.get(currentCheckpointIndex).getDescription();
            else
                return "No description available";
        } else {
            Log.d(LOG, "current race index = " + currentRaceIndex);
            if (races != null && currentRaceIndex >= 0 && currentRaceIndex <= races.size())
                return races.get(currentRaceIndex).getDescription();
            else
                return "No description available";
        }
    }

    /**
     * The question for the current checkpoint or null
     *
     * @return
     */
    @Bindable
    public String getQuestionForCurrentCheckpoint() {
        if (currentCheckpointIndex < checkpoints.size())
            return ((Checkpoint) checkpoints.get(currentCheckpointIndex)).getQuestion();
        return null;
    }

    /**
     * @return the possible answers for the current checkpoint or null
     */
    @Bindable
    public List<String> getPossibleAnswers() {
        if (currentCheckpointIndex < checkpoints.size())
            return ((Checkpoint) checkpoints.get(currentCheckpointIndex)).getPossibleAnswers();
        return null;
    }

    public String getRaceId() {
        if (currentRaceIndex <= races.size())
            return races.get(currentRaceIndex).getId();
        return "";
    }

    private void updateCurrentIndexes(String raceId, String checkpointId) {
        this.currentRaceIndex = getIndexForRaceId(raceId);
        this.currentCheckpointIndex = getIndexForCheckpointId(currentRaceIndex, checkpointId);

        Log.d(LOG, "updated currentRaceIndex to " + currentRaceIndex);

        Log.d(LOG, "notified change");
        notifyChange();
        notifyPropertyChanged(BR.description);
    }

    private int getIndexForRaceId(String raceId) {
        for (int i = 0; i < races.size(); i++) {
            if (races.get(i).getId().equals(raceId)) {
                return i;
            }
        }
        return -1;
    }

    private int getIndexForCheckpointId(int raceIndex, String checkpointId) {
        if (raceIndex == -1) {
            return -1;
        }
        Race race = races.get(currentRaceIndex);
        for (int i = 0; i < race.getCheckpoints().size(); i++) {
            if (race.getCheckpoints().get(i).getId().equals(checkpointId)) {
                return i;
            }
        }
        return -1;
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
                Log.d(LOG, "on tap primary =" + item.isPrimary());
                if (item.isPrimary()) {
                    updateCurrentIndexes(item.getId(), "");
                } else {
                    updateCurrentIndexes(item.getRaceId(), item.getId());
                }
                return true;
            }

            @Override
            public boolean onItemLongPress(final int index, final CustomOverlayItem item) {
                return false;
            }
        };
    }

    public void addDownloadedRaces(List<Race> downloadedRaces) {
        Log.d(LOG, "adding downloaded races");
        int totalAdded = 0;
        if (races != null && races.size() != 0) {
            totalAdded = addAgainstExistingRaces(downloadedRaces);
        } else {
            totalAdded = addAllRaces(downloadedRaces);
        }

        Log.d(LOG, "added " + totalAdded + " races");
    }

    private int addAgainstExistingRaces(List<Race> downloadedRaces) {
        Log.d(LOG, "comparing against previous races");
        int totalAdded = 0;

        for (Race downloadedRace : downloadedRaces) {
            for (Race pastRaces : races) {
                if (pastRaces.getId().equals(downloadedRace.getId())) {
                    Log.d(LOG, "race already present " + downloadedRace.getId());
                    continue;
                }
                races.add(downloadedRace);
                totalAdded++;
            }
        }
        return totalAdded;
    }

    private int addAllRaces(List<Race> downloadedRaces) {
        Log.d(LOG, "no previous races, adding all");
        races = new ArrayList<>();
        for (Race downloadedRace : downloadedRaces) {
            races.add(downloadedRace);
            Log.d(LOG, "added race " + downloadedRace.getId());
        }
        return downloadedRaces.size();
    }

    private Race getActiveRace() {
        if (currentRaceIndex >= 0 && currentRaceIndex < races.size())
            return races.get(currentRaceIndex);
        return new Race();
    }

    /**
     * Use for ExploreFragment. Gives a list of all its races if a race if active or just a list with one race if no race is active.
     * @return
     */
    public List<Race> getRacesForOverlay() {
        if (races == null)
            return new ArrayList<>();
        if (raceActive)
            return new ArrayList<>(Arrays.asList(getActiveRace()));
        return races;
    }

    public Checkpoint getActiveCheckpoint() {
        if (getActiveRace() != null && currentCheckpointIndex >= 0 && currentCheckpointIndex <= getActiveRace().getCheckpoints().size())
            return getActiveRace().getCheckpoints().get(currentCheckpointIndex);
        return new Checkpoint();
    }
}
