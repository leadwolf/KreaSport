package com.ccaroni.kreasport.explore.vm;

/**
 * Created by Master on 17/02/2018.
 */

public interface IExploreVM {

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
