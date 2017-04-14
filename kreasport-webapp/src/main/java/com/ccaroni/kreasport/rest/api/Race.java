package com.ccaroni.kreasport.rest.api;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Master on 13/04/2017.
 */
@Entity
public class Race extends BasePoint {

    @Id
    private String id;

    @Embedded
    private List<Checkpoint> checkpoints;

    private static int dummyIndex = 0;

    public Race() {
        id = new ObjectId().toString();
    }

    public Race(String title, String description, double latitude, double longitude, List<Checkpoint> checkpoints) {
        super(title, description, latitude, longitude);
        id = new ObjectId().toString();
        this.checkpoints = checkpoints;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public static Race getDummyRace() {
        List<Checkpoint> dummyCheckpointList = new ArrayList<>();
        dummyCheckpointList.add(new Checkpoint("Dummy title 1", "Dummy Description 1", "Dummy question 1", 0, 0,
                Arrays.asList("First Question", "Second Question")));
        dummyCheckpointList.add(new Checkpoint("Dummy title 2", "Dummy Description 2", "Dummy question 2", 0, 0,
                Arrays.asList("First Question", "Second Question")));
        return new Race("Dummy Race Title " + dummyIndex, "Dummy Race Description " + dummyIndex, 0, 0, dummyCheckpointList);
    }

}
