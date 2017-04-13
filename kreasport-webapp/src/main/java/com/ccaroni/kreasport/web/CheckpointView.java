package com.ccaroni.kreasport.web;

import com.ccaroni.kreasport.rest.api.Checkpoint;
import com.ccaroni.kreasport.rest.db.CheckpointDAO;
import org.bson.types.ObjectId;
import org.glassfish.jersey.server.mvc.ErrorTemplate;
import org.glassfish.jersey.server.mvc.Template;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Master on 13/04/2017.
 */
@Path("/checkpoint")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.TEXT_HTML)
public class CheckpointView {

    private static CheckpointDAO dao = new CheckpointDAO(false);

    @GET
    @Template
    public List<Checkpoint> getAll() {
        return dao.all();
    }

    @GET
    @Template(name = "detail")
    @Path("/{raceId}/{id}")
    @ErrorTemplate(name = "/error-form")
    public Checkpoint specificCheckpoint(@PathParam("id") String id, @PathParam("raceId") String raceId) {
        return dao.getCheckpointByIds(new ObjectId(id), new ObjectId(raceId));
    }
}
