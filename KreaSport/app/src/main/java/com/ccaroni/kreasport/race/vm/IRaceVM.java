package com.ccaroni.kreasport.race.vm;

/**
 * Created by Master on 17/02/2018.
 */

public interface IRaceVM {

    int getBottomSheetVisibility();

    int getPassiveInfoVisibility();

    int getActiveInfoVisibility();

    String getProgression();

    String getTitle();

    String getDescription();

    void onStartClicked();

    void onStopClicked();

    void onMyLocationClicked();

}
