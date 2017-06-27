package com.ccaroni.kreasport.exception;

/**
 * Created by Master on 28/06/2017.
 */
public class RaceIdNotSameException extends RuntimeException {
    public RaceIdNotSameException(String raceId, String expectedId) {
        super("Received request for id " + expectedId + " but Race id was: " + raceId);
    }
}
