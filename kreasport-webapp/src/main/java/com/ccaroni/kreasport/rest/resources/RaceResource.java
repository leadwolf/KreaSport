package com.ccaroni.kreasport.rest.resources;

import com.ccaroni.kreasport.rest.api.Race;
import com.ccaroni.kreasport.rest.db.RaceRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Master on 13/04/2017.
 */
@Path("/race")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RaceResource {

    @Autowired
    private static RaceRepository raceRepository;

    final static Logger logger = LoggerFactory.getLogger(RaceResource.class);

    public RaceResource() {
        if (raceRepository.count() == 0) {
            for (Race race : Race.getDummyRaces(2)) {
                raceRepository.save(race);
            }
            logger.info("No races, added dummy races");
        }
    }

    @POST
    public void createRace(Race race) {
        raceRepository.save(race);
    }

    @GET
    public List<Race> getAllRaces() {
        return raceRepository.findAll();
    }

    @GET
    @Path("/{id}")
    public Race getRaceById(@PathParam("id") String id) {
        try {
            ObjectId objectId = new ObjectId("id");
        } catch (IllegalArgumentException e) {
            return null;
        }
        return raceRepository.findOne(id);
    }

    @DELETE
    @Path("{id}")
    public void deleteRace(@PathParam("id") String id) {
        raceRepository.delete(id);
    }

}
