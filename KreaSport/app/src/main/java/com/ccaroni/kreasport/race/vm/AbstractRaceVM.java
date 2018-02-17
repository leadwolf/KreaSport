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

    @Inject
    public Box<Race> raceBox;
    public Box<Checkpoint> checkpointBox;
    public Box<Record> recordBox;

    public AbstractRaceVM() {
        App.getBoxComponent().inject(this);
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
        // TODO
        return null;
    }

    @Override
    public String getTitle() {
        // TODO
        return null;
    }

    @Override
    public String getDescription() {
        // TODO
        return null;
    }

    @Override
    public void onStartClicked() {
        // TODO

    }

    @Override
    public void onStopClicked() {
        // TODO

    }

    @Override
    public void onMyLocationClicked() {
        // TODO

    }
}
