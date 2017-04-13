package com.ccaroni.kreasport.rest.resources;

import com.ccaroni.kreasport.dto.UserDto;
import com.ccaroni.kreasport.rest.api.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

@Path("/")
public class LoginResource {
    final static Logger logger = LoggerFactory.getLogger(LoginResource.class);

    @GET
    @Path("/login")
    public UserDto secureWhoAmI(@Context SecurityContext context) {
        User user = (User) context.getUserPrincipal();
        return user.convertToDto();
    }

    @GET
    @Path("/profile")
    @RolesAllowed({"user"})
    public UserDto secureByAnnotation(@Context SecurityContext context) {
        User user = (User) context.getUserPrincipal();
        return user.convertToDto();
    }

}
