package com.ccaroni.kreasport.rest.dto;

import com.ccaroni.kreasport.rest.api.pojo.Checkpoint;
import com.ccaroni.kreasport.rest.api.pojo.Race;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Master on 04/04/2017.
 */
public class RaceDTO extends BasePointDTO {

    private List<CheckpointDTO> checkpointList;

    public RaceDTO() {
    }

    public List<CheckpointDTO> getCheckpointList() {
        return checkpointList;
    }

    public void setCheckpointList(List<CheckpointDTO> checkpointList) {
        this.checkpointList = checkpointList;
    }

    public Race toPOJO() {
        Race race = new Race();

        race.setId(id);
        race.setTitle(title);
        race.setDescription(description);
        race.setLatitude(latitude);
        race.setLongitude(longitude);

        race.setCheckpointList(checkpointListToPOJO(checkpointList));

        return race;
    }

    private List<Checkpoint> checkpointListToPOJO(List<CheckpointDTO> checkpointListDTO) {
        if (checkpointListDTO == null) {
            return null;
        }
        List<Checkpoint> checkpointList = new ArrayList<>();

        for (CheckpointDTO dtoItem : checkpointListDTO) {
            checkpointList.add(dtoItem.toPOJO());
        }

        return checkpointList;
    }

}
