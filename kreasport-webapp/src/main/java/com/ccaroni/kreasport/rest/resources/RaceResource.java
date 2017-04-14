package com.ccaroni.kreasport.rest.resources;

import com.ccaroni.kreasport.rest.api.Checkpoint;
import com.ccaroni.kreasport.rest.api.Race;
import com.ccaroni.kreasport.rest.db.RaceDAO;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Master on 13/04/2017.
 */
@Path("/race")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RaceResource {

    private static RaceDAO dao = new RaceDAO(false);

    final static Logger logger = LoggerFactory.getLogger(RaceResource.class);

    public RaceResource() {
        if (dao.find().asList().size() == 0) {
            List<Checkpoint> dummyCheckpointList = new ArrayList<>();
            dao.save(Race.getDummyRace());
            dao.save(Race.getDummyRace());

            logger.info("No races, added dummy races");
        }
    }

    @POST
    public void createRace(Race race) {
        dao.save(race);
    }

    @GET
    public List<Race> getAllRaces() {
        return dao.getAllRaces();
    }

    @GET
    @Path("/{id}")
    public Race getRaceById(@PathParam("id") String id) {
        try {
            ObjectId objectId = new ObjectId("id");
        } catch (IllegalArgumentException e) {
            return null;
        }
        return dao.getRaceById(new ObjectId(id));
    }

    @DELETE
    @Path("{id}")
    public void deleteRace(@PathParam("id") String id) {
        try {
            ObjectId objectId = new ObjectId("id");
        } catch (IllegalArgumentException e) {
            return;
        }
        dao.remove(dao.getRaceById(new ObjectId(id)));
    }

}
