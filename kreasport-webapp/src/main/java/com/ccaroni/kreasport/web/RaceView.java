package com.ccaroni.kreasport.web;

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
        dao.getCollection().drop();
        if (dao.find().asList().size() == 0) {
            dao.save(Race.getDummyRace());
            dao.save(Race.getDummyRace());

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
