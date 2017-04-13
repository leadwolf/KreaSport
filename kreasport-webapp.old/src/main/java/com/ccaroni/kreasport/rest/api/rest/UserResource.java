package com.ccaroni.kreasport.rest.api.rest;

import com.ccaroni.kreasport.rest.api.db.BDDFactory;
import com.ccaroni.kreasport.rest.api.db.UserDAO;
import com.ccaroni.kreasport.rest.api.pojo.User;
import com.ccaroni.kreasport.rest.dto.UserDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Master on 04/04/2017.
 */

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    private static UserDAO dao = BDDFactory.getDbi().open(UserDAO.class);

    public UserResource() throws SQLException, URISyntaxException {
        if (!BDDFactory.tableExist("users")) {
            dao.createUserTable();
            dao.insert(new User(0, "Margaret Thatcher", "la Dame de fer"));
        }
    }

    @POST
    public UserDTO createUser(UserDTO dto) {
        User user = new User();
        user.initFromDto(dto);
        user.resetPasswordHash();
        int id = dao.insert(user);
        dto.setId(id);
        return dto;
    }

    @GET
    @Path("/{name}")
    public UserDTO getUser(@PathParam("name") String name) {
        User user = dao.findByName(name);
        if (user == null) {
            throw new WebApplicationException(404);
        }
        return user.convertToDto();
    }

    @GET
    public List<UserDTO> getAllUsers(@QueryParam("q") String query) {
        List<User> users;
        if (query == null) {
            users = dao.all();
        } else {
            users = dao.search("%" + query + "%");
        }
        return users.stream().map(User::convertToDto).collect(Collectors.toList());
    }

    @DELETE
    @Path("/{id}")
    public void deleteUser(@PathParam("id") int id) {
        dao.delete(id);
    }

}
