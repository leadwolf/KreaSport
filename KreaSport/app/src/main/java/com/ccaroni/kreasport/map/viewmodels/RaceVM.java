package com.ccaroni.kreasport.map.viewmodels;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;

import org.osmdroid.views.overlay.ItemizedIconOverlay;

import java.util.List;

import com.ccaroni.kreasport.activities.MainActivity;
import com.ccaroni.kreasport.fragments.ExploreFragment;
import com.ccaroni.kreasport.map.views.CustomOverlayItem;
import com.ccaroni.kreasport.map.models.Checkpoint;
import com.ccaroni.kreasport.map.models.Race;

import static com.ccaroni.kreasport.fragments.ExploreFragment.KEY_SELECTED_CHECKPOINT;
import static com.ccaroni.kreasport.fragments.ExploreFragment.KEY_SELECTED_RACE;

/**
 * Created by Master on 06/04/2017.
 * ViewModel for the races. Has a list of {@link Race} as atrtibute and is used by the views to represent that data.
 */
public class RaceVM extends BaseObservable {

    private transient static final String LOG = RaceVM.class.getSimpleName();

    /**
     * The race model to manipulate
     * Transient because we just load all the race data into this VM anyways.
     */
    private transient List<Race> races;

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
    }

    public void setCheckpointVM() {
        checkpoints = races.get(currentRaceIndex).getCheckpoints();
    }

    public int getCheckpointId() {
        if (currentCheckpointIndex < checkpoints.size())
            return checkpoints.get(currentCheckpointIndex).getId();
        return -1;
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
            if (races != null && currentRaceIndex <= races.size())
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
        if (raceActive) {
            if (checkpoints != null && currentCheckpointIndex < checkpoints.size())
                return checkpoints.get(currentCheckpointIndex).getDescription();
            else
                return "No description available";
        } else {
            if (races != null && currentRaceIndex <= races.size())
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

    public int getRaceId() {
        if (currentRaceIndex <= races.size())
            return races.get(currentRaceIndex).getId();
        return -1;
    }

    public void updateCurrentIndexes(int raceIndex, int checkpointIndex) {
        this.currentRaceIndex = raceIndex;
        this.currentCheckpointIndex = checkpointIndex;

        Log.d(LOG, "notified change");
        notifyChange();
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


    /**
     * Gets the listener for a CustomOverlayItem. Takes a {@link ExploreFragment.ExploreInteractionListener} which will be
     * used to send the callback to whatever it's attached to
     *
     * @param mListener
     * @return
     */
    public static ItemizedIconOverlay.OnItemGestureListener<CustomOverlayItem> getIconGestureListener(final ExploreFragment.ExploreInteractionListener mListener) {
        return new ItemizedIconOverlay.OnItemGestureListener<CustomOverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(final int index, final CustomOverlayItem item) {
                Intent request = new Intent();

                request.putExtra(MainActivity.CALLBACK_KEY, ExploreFragment.OVERLAY_ITEM_SELECTION);
                request.putExtra(KEY_SELECTED_RACE, item.getRaceId());
                request.putExtra(KEY_SELECTED_CHECKPOINT, item.getId());

                mListener.onExploreInteraction(request);
                return true;
            }

            @Override
            public boolean onItemLongPress(final int index, final CustomOverlayItem item) {
                return false;
            }
        };
    }
}
