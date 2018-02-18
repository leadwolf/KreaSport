package com.ccaroni.kreasport.race.vm;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

import com.ccaroni.kreasport.race.model.IRaceModel;
import com.ccaroni.kreasport.race.model.impl.RaceModel;

/**
 * Created by Master on 10/02/2018.
 */

public abstract class AbstractRaceVM extends BaseObservable implements IRaceVM {


    protected int bottomSheetVisibility;
    protected int passiveInfoVisibility;
    protected int activeInfoVisibility;

    // TODO instanciate
    private IRaceModel raceModel;

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
    @NonNull
    public String getProgression() {
        // TODO from RaceModel
        return "";
    }

    @Bindable
    @Override
    public @NonNull
    String getTitle() {
        return raceModel.getTitle();
    }

    @Bindable
    @Override
    public @NonNull
    String getDescription() {
        return raceModel.getDescription();
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
