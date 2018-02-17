package com.ccaroni.kreasport.data.local;

import com.ccaroni.kreasport.data.model.ICheckpoint;

import io.objectbox.annotation.Entity;
import io.objectbox.relation.ToOne;

/**
 * Created by Master on 10/02/2018.
 */
@Entity
public class Checkpoint extends MapItem implements ICheckpoint {

    private ToOne<Race> race;

    public Checkpoint() {
    }

    public ToOne<Race> getRace() {
        return race;
    }

    public void setRace(ToOne<Race> race) {
        this.race = race;
    }
}
