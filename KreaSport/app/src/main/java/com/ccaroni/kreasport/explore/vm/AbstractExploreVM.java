package com.ccaroni.kreasport.explore.vm;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.ccaroni.kreasport.explore.exception.IllegalRaceStateException;
import com.ccaroni.kreasport.explore.model.IExploreModel;
import com.ccaroni.kreasport.explore.view.IExploreView;
import com.ccaroni.kreasport.explore.view.activity.App;

import javax.inject.Inject;

/**
 * Created by Master on 10/02/2018.
 */

public abstract class AbstractExploreVM extends BaseObservable implements IExploreVM {

    private static final String TAG = AbstractExploreVM.class.getSimpleName();

    @Inject
    public IExploreModel raceModel;

    protected int bottomSheetVisibility;
    protected int passiveInfoVisibility;
    protected int activeInfoVisibility;

    private IExploreView exploreView;

    public AbstractExploreVM(IExploreView iExploreView) {
        this.exploreView = iExploreView;
        App.getInstance()
                .plusExploreComponent()
                .inject(this);

        updateVisibilities(this.raceModel.isItemSelected(), this.raceModel.isRaceActive());
    }

    /**
     * Updates visibilities for the bottom sheet and notifies bindings.
     */
    private void updateVisibilities(boolean itemSelected, boolean raceActive) {
        this.bottomSheetVisibility = itemSelected ? View.VISIBLE : View.GONE;
        this.activeInfoVisibility = itemSelected ? View.VISIBLE : View.GONE;
        this.passiveInfoVisibility = raceActive ? View.GONE : View.VISIBLE;

        notifyChange();
    }


    @Bindable
    public int getBottomSheetVisibility() {
        return this.bottomSheetVisibility;
    }

    @Bindable
    public int getPassiveInfoVisibility() {
        return this.passiveInfoVisibility;
    }

    @Bindable
    public int getActiveInfoVisibility() {
        return this.activeInfoVisibility;
    }

    @Override
    @NonNull
    public String getProgression() {
        return this.raceModel.getProgression();
    }

    @Bindable
    @Override
    public @NonNull
    String getTitle() {
        return this.raceModel.getTitle();
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
            this.raceModel.requestStartRace();
        } catch (IllegalRaceStateException e) {
            Log.d(TAG, "onStartClicked: " + e);
            this.exploreView.displayStartError(e.getMessage());
        }
    }

    @Override
    public void onStopClicked() {
        try {
            this.raceModel.requestStopRace();
        } catch (IllegalRaceStateException e) {
            Log.d(TAG, "onStartClicked: " + e);
            this.exploreView.displayStopError(e.getMessage());
        }
    }

    @Override
    public void onMyLocationClicked() {
        this.exploreView.recenterOnUserPosition();
    }

    @Override
    public void onRaceSelected(long id) {
        this.raceModel.onRaceSelected(id);
    }

    @Override
    public void onCheckpointSelected(long id) {
        this.raceModel.onCheckpointSelected(id);
    }

    @Override
    public void onBackgroundPressed() {
        this.raceModel.onBackgroundPressed();
    }
}
