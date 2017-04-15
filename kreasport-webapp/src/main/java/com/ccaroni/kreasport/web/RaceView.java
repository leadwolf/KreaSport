package com.ccaroni.kreasport.web;

import com.ccaroni.kreasport.rest.api.Race;
import com.ccaroni.kreasport.rest.db.RaceRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.glassfish.jersey.server.mvc.ErrorTemplate;
import org.glassfish.jersey.server.mvc.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Master on 13/04/2017.
 */
@Path("/race")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.TEXT_HTML)
public class RaceView {

    final static Logger logger = LoggerFactory.getLogger(RaceView.class);

    @Autowired
    private static RaceRepository raceRepository;

    public RaceView() {
        if (raceRepository.count() == 0) {
            for (Race race : Race.getDummyRaces(2)) {
                raceRepository.save(race);
            }
            logger.info("No races, added dummy races");
        }
    }

    @GET
    @Template
    @ErrorTemplate(name = "/error-form")
    public List<String> getAll() {
        List<Race> raceList = raceRepository.findAll();
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
        return raceRepository.findOne(id);
    }
}
