package com.ccaroni.kreasport.dto;

import com.ccaroni.kreasport.api.pojo.PossibleAnswer;
import com.ccaroni.kreasport.api.pojo.Checkpoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Master on 04/04/2017.
 */
public class CheckpointDTO extends BasePointDTO {

    private int raceId;
    private String question;
    private List<PossibleAnswerDTO> possibleAnswers;

    public CheckpointDTO() {
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

    public List<PossibleAnswerDTO> getPossibleAnswers() {
        return possibleAnswers;
    }

    public void setPossibleAnswers(List<PossibleAnswerDTO> possibleAnswers) {
        this.possibleAnswers = possibleAnswers;
    }

    public Checkpoint toPOJO() {
        Checkpoint checkpoint = new Checkpoint();

        checkpoint.setId(id);
        checkpoint.setTitle(title);
        checkpoint.setDescription(description);
        checkpoint.setLatitude(latitude);
        checkpoint.setLongitude(longitude);

        checkpoint.setRaceId(raceId);
        checkpoint.setQuestion(question);
        checkpoint.setPossiblePossibleAnswers(answersToPOJO(possibleAnswers));

        return checkpoint;
    }

    private List<PossibleAnswer> answersToPOJO(List<PossibleAnswerDTO> dto) {
        if (dto == null) {
            return null;
        }
        List<PossibleAnswer> possibleAnswers = new ArrayList<>();
        for (PossibleAnswerDTO possibleAnswerDTO : dto) {
            possibleAnswers.add(possibleAnswerDTO.toPOJO());
        }
        return possibleAnswers;
    }

}
