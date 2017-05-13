package com.example.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Master on 13/04/2017.
 */
@Document(collection = "races")
public class Race extends BasePoint {

    private List<Checkpoint> checkpoints;

    public Race() {

    }

    public Race(String title, String description, double latitude, double longitude, List<Checkpoint> checkpoints) {
        super(title, description, latitude, longitude);
        this.checkpoints = checkpoints;
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

    public static ArrayList<Race> getDummyRaces() {
        ArrayList<Race> dummyRaces = new ArrayList<>();
        List<Checkpoint> dummyCheckpointList = new ArrayList<>();

        // FIRST RACE IUT
        dummyCheckpointList.add(new Checkpoint("Dummy title 1", "Dummy Description 1", "Dummy question 1", 50.613664, 3.136939,
                Arrays.asList("First Question", "Second Question")));
        dummyCheckpointList.add(new Checkpoint("Dummy title 2", "Dummy Description 2", "Dummy question 2", 50.613278, 3.137973,
                Arrays.asList("First Question", "Second Question")));
        dummyRaces.add(new Race("Dummy Race Title 0", "Dummy Race Description 0", 50.613664, 3.136939, dummyCheckpointList));

        // SECOND RACE GLASGOW
        dummyCheckpointList = new ArrayList<>();
        dummyCheckpointList.add(new Checkpoint("Dummy title 1", "Dummy Description 1", "Dummy question 1", 55.866576, -4.251175,
                Arrays.asList("First Question", "Second Question")));
        dummyCheckpointList.add(new Checkpoint("Dummy title 2", "Dummy Description 2", "Dummy question 2", 55.866035, -4.251379,
                Arrays.asList("First Question", "Second Question")));
        dummyRaces.add(new Race("Dummy Race Title 1", "Dummy Race Description 1", 55.866576, -4.251175, dummyCheckpointList));

        return dummyRaces;
    }

}
