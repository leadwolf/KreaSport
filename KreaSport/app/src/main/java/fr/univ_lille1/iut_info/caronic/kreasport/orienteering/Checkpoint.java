package fr.univ_lille1.iut_info.caronic.kreasport.orienteering;

import java.util.List;

/**
 * Created by Master on 05/04/2017.
 */

public class Checkpoint extends BaseItem {

    private int raceId;
    private String question;
    private List<String> possibleAnswers;

    public Checkpoint(int raceId, String question, List<String> possibleAnswers) {
        this.raceId = raceId;
        this.question = question;
        this.possibleAnswers = possibleAnswers;
    }

    public Checkpoint(int id, String title, String description, int raceId, String question, List<String> possibleAnswers) {
        super(id, title, description);
        this.raceId = raceId;
        this.question = question;
        this.possibleAnswers = possibleAnswers;
    }

    public Checkpoint() {

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

    public List<String> getPossibleAnswers() {
        return possibleAnswers;
    }

    public void setPossibleAnswers(List<String> possibleAnswers) {
        this.possibleAnswers = possibleAnswers;
    }
}
