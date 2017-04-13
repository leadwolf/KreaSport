package com.ccaroni.kreasport.rest.api.pojo;

import com.ccaroni.kreasport.rest.dto.PossibleAnswerDTO;

/**
 * Created by Master on 05/04/2017.
 */
public class PossibleAnswer {

    private int id;
    private int checkpointId;
    private String possibleAnswer;

    public PossibleAnswer() {
    }

    public PossibleAnswer(String possibleAnswer) {
        this.possibleAnswer = possibleAnswer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCheckpointId() {
        return checkpointId;
    }

    public void setCheckpointId(int checkpointId) {
        this.checkpointId = checkpointId;
    }

    public String getPossibleAnswer() {
        return possibleAnswer;
    }

    public void setPossibleAnswer(String possibleAnswer) {
        this.possibleAnswer = possibleAnswer;
    }

    public PossibleAnswerDTO toDTO() {
        return new PossibleAnswerDTO(possibleAnswer);
    }
}
