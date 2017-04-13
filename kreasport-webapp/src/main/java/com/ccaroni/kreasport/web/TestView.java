package com.ccaroni.kreasport.web;

import org.glassfish.jersey.server.mvc.Template;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Master on 13/04/2017.
 */
@Path("/test")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.TEXT_HTML)
public class TestView {

    @GET
    @Template
    @Path("/complex")
    public List<String> getAll() {
        List<String> testList = new ArrayList<>();

        testList.add("First");
        testList.add("Second");
        testList.add("Third");

        return testList;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/simple")
    public String getIt() {
        return "Hello, Heroku from Web!";
    }

}
