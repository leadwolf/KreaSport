package com.ccaroni.kreasport.rest.db;

import com.ccaroni.kreasport.rest.api.Checkpoint;
import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import java.util.List;

import static com.ccaroni.kreasport.rest.db.DatastoreFactory.getDB;

/**
 * Created by Master on 13/04/2017.
 */
public class CheckpointDAO extends BasicDAO<Checkpoint, ObjectId> {

    public CheckpointDAO(boolean test) {
        super(getDB(test));
    }

    public void insertCheckpoint(Checkpoint checkpoint) {
        save(checkpoint);
    }

    public Checkpoint getCheckpointByIds(ObjectId checkpointId, ObjectId raceId) {
        Query<Checkpoint> query = createQuery()
                .field("id").equal(checkpointId)
                .field("raceId").equal(raceId);
        return query.get();
    }

    public List<Checkpoint> getCheckpointByRaceId(ObjectId raceId) {
        Query<Checkpoint> query = createQuery()
                .field("raceId").equal(raceId);
        return query.asList();
    }

    public List<Checkpoint> all() {
        List<Checkpoint> list = createQuery().asList();
        if (list.size() == 0) {
            list.add(new Checkpoint(null, "Dummy title", "Dummy description", null, "No Question", null));
        }
        return createQuery().asList();
    }

    public void deleteCheckpoint(Checkpoint checkpoint) {
        delete(checkpoint);
    }

}
