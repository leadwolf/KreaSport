package com.ccaroni.kreasport.rest.db;

import com.ccaroni.kreasport.rest.api.Race;
import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.ccaroni.kreasport.rest.db.DatastoreFactory.getDB;

/**
 * Created by Master on 13/04/2017.
 */
public class RaceDAO extends BasicDAO<Race, ObjectId> {

    final static Logger logger = LoggerFactory.getLogger(RaceDAO.class);

    public RaceDAO(boolean test) {
        super(getDB(test));
    }

    public void saveRace(Race race) {
        save(race);
    }

    public Race getRaceById(ObjectId id) {
        Query<Race> raceQuery = createQuery()
                .field("id").equal(id);
        return raceQuery.get();
    }

    public List<Race> getRacesByQuery(String query, String value) {
        if (query != null && value != null) {
            Query<Race> raceQuery = createQuery()
                    .field(query).equal(value);
            return raceQuery.asList();
        }
        throw new IllegalArgumentException("Query parameters cannot be null");
    }

    public List<Race> getAllRaces() {
        List<Race> races = find().asList();
        logger.info("All races: found " + races.size() + " races");

        return races;
    }

    /**
     * Updates a race based upon the id
     * @param race
     */
    public void updateRace(Race race) {
        getDatastore().merge(race);
    }

    public void remove(Race race) {
        delete(race);
    }

}
