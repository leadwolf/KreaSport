package com.ccaroni.kreasport.explore.vm;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

import com.ccaroni.kreasport.explore.model.IExploreModel;
import com.ccaroni.kreasport.explore.view.activity.App;

import javax.inject.Inject;

/**
 * Created by Master on 10/02/2018.
 */

public abstract class AbstractExploreVM extends BaseObservable implements IExploreVM {


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
        // TODO from ExploreModel
        return "";
    }

    @Bindable
    @Override
    public @NonNull
    String getTitle() {
        return null;
    }

    @Bindable
    @Override
    public @NonNull
    String getDescription() {
        return null;
    }

    @Override
    public void onStartClicked() {
        // TODO verify with ExploreModel

    }

    @Override
    public void onStopClicked() {
        // TODO verify with ExploreModel

    }

    @Override
    public void onMyLocationClicked() {
        // TODO call fragment

    }
}
