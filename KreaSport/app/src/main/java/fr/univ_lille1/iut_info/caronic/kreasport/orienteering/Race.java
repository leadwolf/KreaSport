package fr.univ_lille1.iut_info.caronic.kreasport.orienteering;

import java.util.List;

/**
 * Created by Master on 05/04/2017.
 */

public class Race extends BaseItem {

    private List<Checkpoint> checkpoints;

    public Race(List<Checkpoint> checkpoints) {
        this.checkpoints = checkpoints;
    }

    public Race(int id, String title, String description, List<Checkpoint> checkpoints) {
        super(id, title, description);
        this.checkpoints = checkpoints;
    }

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(List<Checkpoint> checkpoints) {
        this.checkpoints = checkpoints;
    }

    public Checkpoint getCheckpoint(int checkpointIndex) {
        if (checkpoints == null) {
            return null;
        }
        return checkpoints.get(checkpointIndex);
    }
}
