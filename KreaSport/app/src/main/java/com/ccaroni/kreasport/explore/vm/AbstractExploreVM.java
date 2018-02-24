package com.ccaroni.kreasport.explore.vm;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ccaroni.kreasport.explore.exception.IllegalRaceStateException;
import com.ccaroni.kreasport.explore.model.IExploreModel;
import com.ccaroni.kreasport.explore.view.activity.App;

import javax.inject.Inject;

/**
 * Created by Master on 10/02/2018.
 */

public abstract class AbstractExploreVM extends BaseObservable implements IExploreVM {

    private static final String TAG = AbstractExploreVM.class.getSimpleName();

    protected int bottomSheetVisibility;
    protected int passiveInfoVisibility;
    protected int activeInfoVisibility;

    @Inject
    public IExploreModel raceModel;

    public AbstractExploreVM() {
        App.getInstance()
                .plusExploreComponent()
                .inject(this);
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
        return raceModel.getProgression();
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
        return raceModel.getTitle();
    }

    @Override
    public void onStartClicked() {
        try {
            raceModel.requestStartRace();
        } catch (IllegalRaceStateException e) {
            Log.d(TAG, "onStartClicked: " + e);
            // TODO display error
        }
    }

    @Override
    public void onStopClicked() {
        try {
            raceModel.requestStopRace();
        } catch (IllegalRaceStateException e) {
            Log.d(TAG, "onStartClicked: " + e);
            // TODO display error
        }
    }

    @Override
    public void onMyLocationClicked() {
        // TODO call fragment
    }
}
