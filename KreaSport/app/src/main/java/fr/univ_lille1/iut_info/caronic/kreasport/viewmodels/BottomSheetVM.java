package fr.univ_lille1.iut_info.caronic.kreasport.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import fr.univ_lille1.iut_info.caronic.kreasport.map.orienteering.Checkpoint;
import fr.univ_lille1.iut_info.caronic.kreasport.map.orienteering.Race;

/**
 * Created by Master on 06/04/2017.
 */

public class BottomSheetVM extends BaseObservable {

    private static final String LOG = BottomSheetVM.class.getSimpleName();

    private List<Checkpoint> checkpoints;
    private List<Race> races;
    private int currentRaceIndex;
    private int currentCheckpointIndex;
    private boolean raceActive;

    private int passiveInfoVisibility;
    private int activeInfoVisbility;

    public BottomSheetVM(List<Race> raceList) {
        this.races = raceList;
        raceActive = false;
        currentRaceIndex = 0;
        currentCheckpointIndex = 0;
        checkpoints = new ArrayList<>();
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
     *
     * @return the title for the current race if a race is not active, or the checkpoints description if active
     */
    @Bindable
    public String getTitle() {
        if (raceActive) {
            if (currentCheckpointIndex < checkpoints.size())
                return checkpoints.get(currentCheckpointIndex).getTitle();
            return null;
        } else {
            if (currentRaceIndex <= races.size())
                return races.get(currentRaceIndex).getTitle();
            return null;
        }
    }

    /**
     *
     * @return the description for the current race if a race is not active, or the checkpoints description if active
     */
    @Bindable
    public String getDescription() {
        if (raceActive) {
            if (currentCheckpointIndex < checkpoints.size())
                return checkpoints.get(currentCheckpointIndex).getDescription();
            return null;
        } else {
            if (currentRaceIndex <= races.size())
                return races.get(currentRaceIndex).getDescription();
            return null;
        }
    }

    /**
     * The question for the current checkpoint or null
     * @return
     */
    @Bindable
    public String getQuestionForCurrentCheckpoint() {
        if (currentCheckpointIndex < checkpoints.size())
            return ((Checkpoint) checkpoints.get(currentCheckpointIndex)).getQuestion();
        return null;
    }

    /**
     *
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
}
