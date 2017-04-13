package com.ccaroni.kreasport.web;

import com.ccaroni.kreasport.rest.api.db.BDDFactory;
import com.ccaroni.kreasport.rest.api.db.UserDAO;
import com.ccaroni.kreasport.rest.api.pojo.User;
import org.glassfish.jersey.server.mvc.Template;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Master on 06/04/2017.
 */
@Path("/htmluser")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.TEXT_HTML)
public class UserView {

    private static UserDAO dao = BDDFactory.getDbi().open(UserDAO.class);

    @GET
    @Template
    public List<User> getAll() {
        return dao.all();
    }

    @GET
    @Template(name = "detail")
    @Path("/{id}")
    public User getDetail(@PathParam("id") String id) {
        return dao.findById(Integer.parseInt(id));
    }

}
