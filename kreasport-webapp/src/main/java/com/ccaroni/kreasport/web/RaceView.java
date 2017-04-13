package com.ccaroni.kreasport.web;

import com.ccaroni.kreasport.rest.api.Checkpoint;
import com.ccaroni.kreasport.rest.api.Race;
import com.ccaroni.kreasport.rest.db.RaceDAO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.glassfish.jersey.server.mvc.ErrorTemplate;
import org.glassfish.jersey.server.mvc.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Master on 13/04/2017.
 */
@Path("/race")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.TEXT_HTML)
public class RaceView {

    final static Logger logger = LoggerFactory.getLogger(RaceView.class);

    private static RaceDAO dao = new RaceDAO(false);

    public RaceView() {
        if (dao.find().asList().size() == 0) {
            List<Checkpoint> dummyCheckpointList = new ArrayList<>();
            dummyCheckpointList.add(new Checkpoint("Dummy title 1", "Dummy Description 1", "Dummy question 1",
                    Arrays.asList("First Question", "Second Question")));
            dummyCheckpointList.add(new Checkpoint("Dummy title 2", "Dummy Description 2", "Dummy question 2",
                    Arrays.asList("First Question", "Second Question")));
            dao.save(new Race("Dummy Race Title 1", "Dummy Race Description 1", dummyCheckpointList));
            dao.save(new Race("Dummy Race Title 2", "Dummy Race Description 2", dummyCheckpointList));

            logger.info("No races, added dummy races");
        }
    }

    @GET
    @Template
    @ErrorTemplate(name = "/error-form")
    public List<String> getAll() {
        List<Race> raceList = dao.getAllRaces();
        List<String> jsonList = new ArrayList<>();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        for (Race race : raceList) {
            jsonList.add(gson.toJson(race, Race.class));
        }
        return jsonList;
    }

    @GET
    @Template(name = "detail")
    @Path("/{id}")
    @ErrorTemplate(name = "/error-form")
    public Race specificCheckpoint(@PathParam("id") String id) {
        return dao.getRacesByQuery("id", id).get(0);
    }
}
