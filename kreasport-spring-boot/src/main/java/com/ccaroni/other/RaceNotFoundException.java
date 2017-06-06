package com.ccaroni.other;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Master on 16/04/2017.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Could not find race")
public class RaceNotFoundException extends RuntimeException {

    public RaceNotFoundException(String id) {
        super("Could not find race with id:" + id);
    }
}
