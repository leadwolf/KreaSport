package com.caroni.kreasport.controller;

import com.caroni.kreasport.domain.Race;
import com.caroni.kreasport.domain.exception.RaceNotFoundException;
import com.caroni.kreasport.repository.RaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by Master on 12/05/2017.
 */
@RestController
@RequestMapping("/races")
public class RaceController {

    @Autowired
    private RaceRepository raceRepository;

    public RaceController(RaceRepository raceRepository) {
    }

    @RequestMapping(method = GET)
    public List<Race> getAllRaces(Principal principal) {
        return raceRepository.findAll();
    }


    @RequestMapping(method = GET, value = "/{id}")
    public Race getRaceById(@PathVariable String id) {
        validateRace(id);
        return raceRepository.findById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(method = DELETE, value = "/{id}")
    public void deleteRaceById(@PathVariable String id) {
        validateRace(id);
        raceRepository.deleteById(id);
    }

    private void validateRace(String id) {
        if (raceRepository.findById(id) == null) {
            throw new RaceNotFoundException(id);
        }
    }
}
