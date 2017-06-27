package com.ccaroni.kreasport.controller;

import com.ccaroni.kreasport.dto.Race;
import com.ccaroni.kreasport.exception.RaceIdNotSameException;
import com.ccaroni.kreasport.exception.RaceNotFoundException;
import com.ccaroni.kreasport.repository.RaceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by Master on 16/04/2017.
 */
@RestController
@RequestMapping("/races")
public class RaceController {

    @Autowired
    private RaceRepository raceRepository;

    final static Logger logger = LoggerFactory.getLogger(RaceController.class);


    /**
     * Use PUT to create a {@link Race} at the given id or totally modify.
     * @param race
     * @return
     */
    @RequestMapping(method = PUT, path = "/{id}")
    private ResponseEntity<?> createRace(@RequestBody Race race, @PathVariable("id") String id) {

        verifyExpectedRaceId(race.getId(), id);

        raceRepository.save(race);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("{id}")
                .buildAndExpand(race.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @RequestMapping(method = GET)
    public List<Race> all() {
        if (raceRepository.count() == 0) {
            for (Race race : Race.getDummyRaces()) {
                raceRepository.save(race);
            }
            logger.info("No races, added dummy races");
        }
        return raceRepository.findAll();
    }

    @RequestMapping(path = "/{id}", method = GET)
    public Race getRaceById(@PathVariable("id") String id) {
        verifyRaceExists(id);
        return raceRepository.findById(id).get();
    }

    @RequestMapping(path = "{id}", method = DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteRaceById(@PathVariable("id") String id) {
        verifyRaceExists(id);
        raceRepository.deleteById(id);
    }


    private void verifyRaceExists(String id) {
        if (!raceRepository.findById(id).isPresent())
            throw new RaceNotFoundException(id);

    }

    private void verifyExpectedRaceId(String raceId, String expectedId) {
        if (!raceId.equals(expectedId)) {
            throw new RaceIdNotSameException(raceId, expectedId);
        }
    }


}
