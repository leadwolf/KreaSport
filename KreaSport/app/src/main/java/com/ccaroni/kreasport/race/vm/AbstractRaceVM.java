package com.ccaroni.kreasport.race.vm;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.ccaroni.kreasport.data.local.Checkpoint;
import com.ccaroni.kreasport.data.local.Race;
import com.ccaroni.kreasport.data.local.Record;
import com.ccaroni.kreasport.race.view.activity.App;

import javax.inject.Inject;

import io.objectbox.Box;

/**
 * Created by Master on 10/02/2018.
 */

public abstract class AbstractRaceVM extends BaseObservable implements IRaceVM {


    protected int bottomSheetVisibility;
    protected int passiveInfoVisibility;
    protected int activeInfoVisibility;

    public AbstractRaceVM() {

    }

    @Bindable
    public int getBottomSheetVisibility() {
        return bottomSheetVisibility;
    }

    @Bindable
    public int getPassiveInfoVisibility() {
        return passiveInfoVisibility;
    }

    @Bindable
    public int getActiveInfoVisibility() {
        return activeInfoVisibility;
    }

    @Override
    public String getProgression() {
        // TODO from RaceModel
        return null;
    }

    @Override
    public String getTitle() {
        // TODO from RaceModel
        return null;
    }

    @Override
    public String getDescription() {
        // TODO from RaceModel
        return null;
    }

    @Override
    public void onStartClicked() {
        // TODO verify with RaceModel

    }

    @Override
    public void onStopClicked() {
        // TODO verify with RaceModel

    }

    @Override
    public void onMyLocationClicked() {
        // TODO call fragment

    }
}
