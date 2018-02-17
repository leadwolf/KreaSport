package com.ccaroni.kreasport.data.local;

import com.ccaroni.kreasport.data.model.IRace;

import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.relation.ToMany;

/**
 * Created by Master on 10/02/2018.
 */
@Entity
public class Race extends MapItem implements IRace {

    @Backlink
    private ToMany<Checkpoint> checkpoints;

    public Race() {
    }

    public ToMany<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(ToMany<Checkpoint> checkpoints) {
        this.checkpoints = checkpoints;
    }

}