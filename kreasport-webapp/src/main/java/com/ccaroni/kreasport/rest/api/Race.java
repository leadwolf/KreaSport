package com.ccaroni.kreasport.rest.api;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.List;

/**
 * Created by Master on 13/04/2017.
 */
@Entity
public class Race extends BasePoint {

    @Id
    ObjectId id;

    @Embedded
    List<Checkpoint> checkpoints;

    public Race() {
    }

    public Race(String title, String description, List<Checkpoint> checkpoints) {
        super(title, description);
        this.checkpoints = checkpoints;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(List<Checkpoint> checkpoints) {
        this.checkpoints = checkpoints;
    }

    public void addCheckpoint(Checkpoint checkpoint) {
        checkpoints.add(checkpoint);
    }

    public void insertCheckpointAtIndex(Checkpoint checkpoint, int index) {
        checkpoints.add(index, checkpoint);
    }
}
