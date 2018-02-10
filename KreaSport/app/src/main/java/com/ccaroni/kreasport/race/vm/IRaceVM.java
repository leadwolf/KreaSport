package com.ccaroni.kreasport.race.vm;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by Master on 10/02/2018.
 */

public abstract class IRaceVM extends BaseObservable {

    protected int bottomSheetVisibility;
    protected int passiveInfoVisibility;
    protected int activeInfoVisibility;


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
