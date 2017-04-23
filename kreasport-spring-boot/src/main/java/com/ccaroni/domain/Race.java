package com.ccaroni.domain;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Master on 13/04/2017.
 */
@Document(collection = "races")
public class Race extends BasePoint {

    @Id
    private String id;
    private List<Checkpoint> checkpoints;

    @Transient
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

    public static ArrayList<Race> getDummyRaces(int count) {
        ArrayList<Race> dummyRaces = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            List<Checkpoint> dummyCheckpointList = new ArrayList<>();
            dummyCheckpointList.add(new Checkpoint("Dummy title 1", "Dummy Description 1", "Dummy question 1", 50.613664, 3.136939,
                    Arrays.asList("First Question", "Second Question")));
            dummyCheckpointList.add(new Checkpoint("Dummy title 2", "Dummy Description 2", "Dummy question 2", 50.613278, 3.137973,
                    Arrays.asList("First Question", "Second Question")));
            dummyRaces.add(new Race("Dummy Race Title " + dummyIndex, "Dummy Race Description " + dummyIndex, 50.613664, 3.136939, dummyCheckpointList));
            dummyIndex++;
        }
        return dummyRaces;
    }

}
