package com.ccaroni.kreasport.rest.api.pojo;

import com.ccaroni.kreasport.rest.dto.CheckpointDTO;
import com.ccaroni.kreasport.rest.dto.RaceDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Master on 04/04/2017.
 */
public class Race extends BasePoint {

    private List<Checkpoint> checkpointList;

    public Race() {
    }

    public Race(int id, String title, String description, List<Checkpoint> checkpointList) {
        super(id, title, description);
        this.checkpointList = checkpointList;
    }

    public List<Checkpoint> getCheckpointList() {
        return checkpointList;
    }

    public void setCheckpointList(List<Checkpoint> checkpointList) {
        this.checkpointList = checkpointList;
    }

    public RaceDTO toDTO() {
        RaceDTO raceDto = new RaceDTO();

        raceDto.setId(id);
        raceDto.setTitle(title);
        raceDto.setDescription(description);
        raceDto.setLatitude(latitude);
        raceDto.setLongitude(longitude);

        raceDto.setCheckpointList(checkpointListToDTO(checkpointList));

        return raceDto;
    }

    private List<CheckpointDTO> checkpointListToDTO(List<Checkpoint> checkpointList) {
        if (checkpointList == null) {
            return null;
        }
        List<CheckpointDTO> checkpointListDTO = new ArrayList<>();

        for (Checkpoint checkpoint : checkpointList) {
            checkpointListDTO.add(checkpoint.toDTO());
        }

        return checkpointListDTO;
    }

}
