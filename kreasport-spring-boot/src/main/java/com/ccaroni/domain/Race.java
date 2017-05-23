package com.ccaroni.domain;

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

    /**
     * Constructor for de/se-rializing
     */
    public Race() {
        super();
    }

    private Race(String title, String description, double latitude, double longitude) {
        super(title, description, latitude, longitude);
    }

    public Race(String title, String description, double latitude, double longitude, List<Checkpoint> checkpoints) {
        this(title, description, latitude, longitude);
        this.checkpoints = checkpoints;
    }

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }


    /**
     * Replaces the current list of checkpoints with checkpointsToAdd. Sets their {@link CheckpointKey}s.
     *
     * @param checkpointsToAdd
     */
    public void setCheckpoints(List<Checkpoint> checkpointsToAdd) {
        this.checkpoints = checkpointsToAdd;
        int order = 0;
        for (Checkpoint checkpoint : this.checkpoints) {
            checkpoint.setCheckpointKey(new CheckpointKey(getId(), order));
            order++;
        }
    }

    /**
     * Adds a list of checkpoints to the current list, and their {@link CheckpointKey}s.
     *
     * @param checkpointsToAdd
     */
    public void addCheckpoints(List<Checkpoint> checkpointsToAdd) {
        int order = this.checkpoints.size() + 1;
        for (Checkpoint checkpoint : this.checkpoints) {
            checkpoint.setCheckpointKey(new CheckpointKey(getId(), order));
            this.checkpoints.add(checkpoint);
            order++;
        }
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
        Race race = new Race("Dummy Race Title 0", "Dummy Race Description 0", 50.613664, 3.136939);

        dummyCheckpointList.add(
                new Checkpoint("Dummy Checkpoint title 0", "Dummy Checkpoint Description 0", 50.613144, 3.138257,
                        new Riddle()
                                .setQuestion("What answer? (0)")
                                .setAnswers(Arrays.asList("Answer 0", "Answer 1"))
                                .setAnswerIndex(0)
                )
        );
        dummyCheckpointList.add(
                new Checkpoint("Dummy Checkpoint title 1", "Dummy Checkpoint Description 1", 50.612489, 3.139485,
                        new Riddle()
                                .setQuestion("What answer? (1)")
                                .setAnswers(Arrays.asList("Answer 0", "Answer 1"))
                                .setAnswerIndex(1)
                )
        );
        race.setCheckpoints(dummyCheckpointList);
        dummyRaces.add(race);

        // SECOND RACE GLASGOW
        race = new Race("Dummy Race Title 1", "Dummy Race Description 1", 55.866576, -4.251175);

        dummyCheckpointList = new ArrayList<>();
        dummyCheckpointList.add(
                new Checkpoint("Dummy Checkpoint title 0", "Dummy Checkpoint Description 0", 55.866363, -4.253852,
                        new Riddle()
                                .setQuestion("What answer? (0)")
                                .setAnswers(Arrays.asList("Answer 0", "Answer 1"))
                                .setAnswerIndex(0)
                )
        );
        dummyCheckpointList.add(
                new Checkpoint("Dummy Checkpoint title 1", "Dummy Checkpoint Description 1", 55.866823, -4.256497,
                        new Riddle()
                                .setQuestion("What answer? (1)")
                                .setAnswers(Arrays.asList("Answer 0", "Answer 1"))
                                .setAnswerIndex(1)
                )
        );
        race.setCheckpoints(dummyCheckpointList);
        dummyRaces.add(race);

        return dummyRaces;
    }

}
