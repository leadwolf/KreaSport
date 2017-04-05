package fr.univ_lille1.iut_info.caronic.kreasport.maps;

import fr.univ_lille1.iut_info.caronic.kreasport.orienteering.Race;

/**
 * Created by Master on 05/04/2017.
 */

public class RaceState {

    private boolean ongoing;
    private Race race;

    public RaceState(boolean state) {
        ongoing = state;
    }

    public boolean isOngoing() {
        return ongoing;
    }

    public void setOngoing(boolean ongoing) {
        this.ongoing = ongoing;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }
}
