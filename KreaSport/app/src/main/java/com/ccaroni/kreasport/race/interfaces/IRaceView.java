package com.ccaroni.kreasport.race.interfaces;

import android.location.Location;

import com.ccaroni.kreasport.data.dto.Riddle;
import com.ccaroni.kreasport.data.realm.RealmCheckpoint;
import com.ccaroni.kreasport.map.views.CustomOverlayItem;
import com.ccaroni.kreasport.race.RaceVM;

import org.osmdroid.util.GeoPoint;

import java.util.List;

/**
 * Created by Master on 20/08/2017.
 * This interfaces specifies methods that the "explore" activity must implement for {@link RaceVM} to automatically update the view
 */
public interface IRaceView {

    /**
     * Center the map to the current location.
     */
    void onMyLocationClicked();

    /**
     * Ask the user to confirm the stopping of the race.
     */
    void askStopConfirmation();

    /**
     * Displays a message to the user.
     *
     * @param message the message to display
     */
    void toast(String message);

    /**
     * @return the last known location in this activity context
     */
    Location getLastKnownLocation();

    /**
     * If the current map is not centered on startPoint and a move to startPoint would need to be animated
     *
     * @param startPoint a point on which the map should now be centered
     * @return if the move needs to be animated
     */
    boolean needToAnimateToPoint(GeoPoint startPoint);

    /**
     * Starts the chronometer stopwatch.
     *
     * @param timeStart the base time to set for the chronometer
     */
    void startChronometer(long timeStart);

    /**
     * Stops the chronometer stopwatch.
     */
    void stopChronometer();

    /**
     * Prompts the user to respond to a ridlle
     *
     * @param riddle the riddle to ask
     */
    void askRiddle(Riddle riddle);

    /**
     * Adds a geofence with this activity's context
     *
     * @param targetingCheckpoint the checkpoint with the coordinates where the geofence should be
     */
    void addGeoFence(RealmCheckpoint targetingCheckpoint);

    /**
     * Reveals the next checkpoint on the map
     *
     * @param customOverlayItem the {@link CustomOverlayItem} to display
     */
    void revealNextCheckpoint(CustomOverlayItem customOverlayItem);

    /**
     * Remove the last geofence.
     */
    void removeLastGeofence();

    /**
     * @return if the user needs to accept google location settings
     */
    boolean userShouldVerifyLocationSettings();

    void askResolveLocationSettings();

    /**
     * Clear all previous markers and add overlayItemsList to the map.
     *
     * @param overlayItemsList a list of {@link CustomOverlayItem} that correspond to one single race
     */
    void focusOnRace(List<CustomOverlayItem> overlayItemsList);

    void setDefaultMarkers();
}
