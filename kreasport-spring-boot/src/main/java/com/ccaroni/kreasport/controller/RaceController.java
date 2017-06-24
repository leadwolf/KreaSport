package com.ccaroni.kreasport.controller;

import com.ccaroni.kreasport.dto.Race;
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

    @RequestMapping(method = POST)
    public ResponseEntity<?> createRace(@RequestBody Race race) {
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
        validateRace(id);
        return raceRepository.findById(id).get();
    }

    @RequestMapping(path = "{id}", method = DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteRaceById(@PathVariable("id") String id) {
        validateRace(id);
        raceRepository.deleteById(id);
    }


    private void validateRace(String id) {
        if (!raceRepository.findById(id).isPresent())
            throw new RaceNotFoundException(id);

    }


}
