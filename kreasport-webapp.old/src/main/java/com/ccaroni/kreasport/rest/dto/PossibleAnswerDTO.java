package com.ccaroni.kreasport.rest.dto;

import com.ccaroni.kreasport.rest.api.pojo.PossibleAnswer;

/**
 * Created by Master on 05/04/2017.
 */
public class PossibleAnswerDTO {

    private String possibleAnswer;

    public PossibleAnswerDTO() {
    }

    public PossibleAnswerDTO(String possibleAnswer) {
        this.possibleAnswer = possibleAnswer;
    }

    public String getPossibleAnswer() {
        return possibleAnswer;
    }

    public void setPossibleAnswer(String possibleAnswer) {
        this.possibleAnswer = possibleAnswer;
    }

    public PossibleAnswer toPOJO() {
        return new PossibleAnswer(possibleAnswer);
    }
}
