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

public abstract class IRaceVM extends BaseObservable {

    protected int bottomSheetVisibility;
    protected int passiveInfoVisibility;
    protected int activeInfoVisibility;

    @Inject
    public Box<Race> raceBox;
    public Box<Checkpoint> checkpointBox;
    public Box<Record> recordBox;

    public IRaceVM() {
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

    @Bindable
    public String getProgression() {
        // TODO
        return "";
    }

    @Bindable
    public String getTitle() {
        // TODO
        return "";
    }

    @Bindable
    public String getDescription() {
        // TODO
        return "";
    }

    public void onStopClicked() {
        // TODO
    }

}
