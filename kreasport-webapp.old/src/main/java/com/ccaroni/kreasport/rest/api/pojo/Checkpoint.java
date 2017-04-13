package com.ccaroni.kreasport.rest.api.pojo;

import com.ccaroni.kreasport.rest.dto.PossibleAnswerDTO;
import com.ccaroni.kreasport.rest.dto.CheckpointDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Master on 04/04/2017.
 */
public class Checkpoint extends BasePoint {

    private int raceId;
    private String question;
    private List<PossibleAnswer> possiblePossibleAnswers;

    public Checkpoint() {
    }

    public Checkpoint(int id, String title, String description, int raceId, String question, List<PossibleAnswer> possiblePossibleAnswers) {
        super(id, title, description);
        this.raceId = raceId;
        this.question = question;
        this.possiblePossibleAnswers = possiblePossibleAnswers;
    }

    public int getRaceId() {
        return raceId;
    }

    public void setRaceId(int raceId) {
        this.raceId = raceId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<PossibleAnswer> getPossiblePossibleAnswers() {
        return possiblePossibleAnswers;
    }

    public void setPossiblePossibleAnswers(List<PossibleAnswer> possiblePossibleAnswers) {
        this.possiblePossibleAnswers = possiblePossibleAnswers;
    }

    public CheckpointDTO toDTO() {
        CheckpointDTO checkpointDto = new CheckpointDTO();

        checkpointDto.setId(id);
        checkpointDto.setTitle(title);
        checkpointDto.setDescription(description);
        checkpointDto.setLatitude(latitude);
        checkpointDto.setLongitude(longitude);

        checkpointDto.setRaceId(raceId);
        checkpointDto.setQuestion(question);
        checkpointDto.setPossibleAnswers(answersToDTO(possiblePossibleAnswers));

        return checkpointDto;
    }

    private List<PossibleAnswerDTO> answersToDTO(List<PossibleAnswer> possibleAnswers) {
        if (possibleAnswers == null) {
            return null;
        }
        List<PossibleAnswerDTO> answersDTO = new ArrayList<>();
        for (PossibleAnswer possibleAnswer : possibleAnswers) {
            answersDTO.add(possibleAnswer.toDTO());
        }
        return answersDTO;
    }

}
