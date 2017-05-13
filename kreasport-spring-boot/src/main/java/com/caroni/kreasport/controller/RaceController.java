package com.caroni.kreasport.controller;

import com.caroni.kreasport.domain.Race;
import com.caroni.kreasport.repository.RaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

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
}
