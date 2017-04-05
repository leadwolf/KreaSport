package com.ccaroni.kreasport.api.rest;

import com.ccaroni.kreasport.api.db.CheckpointDAO;
import com.ccaroni.kreasport.api.db.PossibleAnswerDAO;
import com.ccaroni.kreasport.api.pojo.Checkpoint;
import com.ccaroni.kreasport.api.pojo.PossibleAnswer;
import com.ccaroni.kreasport.dto.CheckpointDTO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.ccaroni.kreasport.api.db.BDDFactory.getDbi;
import static com.ccaroni.kreasport.api.db.BDDFactory.tableExist;

/**
 * Created by Master on 05/04/2017.
 */
@Path("/checkpoint")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CheckpointResource {

    private static CheckpointDAO dao = getDbi().open(CheckpointDAO.class);
    private static PossibleAnswerDAO possibleAnswerDAO = getDbi().open(PossibleAnswerDAO.class);

    public CheckpointResource() throws SQLException, URISyntaxException {
        if (!tableExist("checkpoints")) {
            dao.createCheckpointTable();
            dao.insert(new Checkpoint(-1, "Dummy title", "Dummy description", -1, "Dummy question", null));
        }
    }

    @POST
    public CheckpointDTO createUser(CheckpointDTO dto) {
        Checkpoint checkpoint = dto.toPOJO();

        int id = dao.insert(checkpoint);
        dto.setId(id);

        return dto;
    }

    @GET
    @Path("/{raceId}/{id}")
    public CheckpointDTO getCheckpoint(@PathParam("id") int id, @PathParam("raceId") int raceId) {
        Checkpoint checkpoint = dao.findByID(id, raceId);
        if (checkpoint == null) {
            throw new WebApplicationException(404);
        }
        return checkpoint.toDTO();
    }

    @GET
    @Path("/{raceId}")
    public List<CheckpointDTO> getCheckpointsForRace(@PathParam("raceId") int raceId) {
        List<Checkpoint> checkpointList = dao.findByRaceID(raceId);
        if (checkpointList == null) {
            throw new WebApplicationException(404);
        }
        List<CheckpointDTO> checkpointDTOList = new ArrayList<>();
        for (Checkpoint checkpoint : checkpointList) {
            checkpointDTOList.add(checkpoint.toDTO());
        }
        return checkpointDTOList;
    }

    @GET
    public List<CheckpointDTO> getAllCheckpoints() {
        List<Checkpoint> checkpointList = dao.all();
        if (checkpointList == null) {
            throw new WebApplicationException(404);
        }
        for (Checkpoint checkpoint : checkpointList) {
            List<PossibleAnswer> possibleAnswers = possibleAnswerDAO.findByCheckpointId(checkpoint.getId());
            checkpoint.setPossiblePossibleAnswers(possibleAnswers);
        }
        return checkpointList.stream().map(Checkpoint::toDTO).collect(Collectors.toList());
    }

    @DELETE
    @Path("/{raceId}")
    public void deleteUser(@PathParam("raceId") int raceId) {
        dao.deleteAllForRace(raceId);
    }

    @DELETE
    @Path("/{raceId}/{id}")
    public void deleteUser(@PathParam("raceId") int raceId, @PathParam("id") int id) {
        dao.delete(raceId, id);
    }

}
