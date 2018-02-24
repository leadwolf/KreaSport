package com.ccaroni.kreasport.explore.view;

/**
 * Created by Master on 24/02/2018.
 * Contract that the view exposing the map implements, exposing methods to trigger UI changes.
 */
public interface IExploreView {

    /**
     * Displays the error preventing the start of the recording
     *
     * @param message the cause
     */
    void displayStartError(String message);

    /**
     * Displays the error preventing the stopping of the recording
     *
     * @param message the cause
     */
    void displayStopError(String message);

    /**
     * Re-centers the map to the user's last known location
     */
    void recenterOnUserPosition();
}
