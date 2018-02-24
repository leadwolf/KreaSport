package com.ccaroni.kreasport.explore.vm;

/**
 * Created by Master on 17/02/2018.
 */

public interface IExploreVM {

    /**
     * @return the visible state of the bottom sheet
     */
    int getBottomSheetVisibility();

    /**
     * @return the visible state of the passive bottom sheet header
     */
    int getPassiveInfoVisibility();

    /**
     * @return the visible state of the active bottom sheet header
     */
    int getActiveInfoVisibility();

    /**
     * @return the checkpoint progression
     */
    String getProgression();

    /**
     * @return the title cf the selected item
     */
    String getTitle();

    /**
     * @return the description of the selected item
     */
    String getDescription();

    /**
     * Invoked when the user clicks on the start race button. Triggers an attempt to start a recording for the race corresponding to the selected item.
     */
    void onStartClicked();

    /**
     * Invoked when the user clicks on the stop race button. Triggers an attempt to stop the current recording.
     */
    void onStopClicked();

    /**
     * Invoked when the user clicks on the my location button.
     */
    void onMyLocationClicked();

    /**
     * @param id the id of the race clicked on
     */
    void onRaceSelected(long id);

    /**
     * @param id the id of the checkpoint clicked on
     */
    void onCheckpointSelected(long id);

}
