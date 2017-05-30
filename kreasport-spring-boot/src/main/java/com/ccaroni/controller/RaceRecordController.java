package com.ccaroni.controller;

import com.ccaroni.domain.RaceRecord;
import com.ccaroni.other.RaceRecordNotFoundException;
import com.ccaroni.repository.RaceRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by Master on 30/05/2017.
 */
@RequestMapping("/records")
public class RaceRecordController {

    @Autowired
    private RaceRecordRepository raceRecordRepository;

    @RequestMapping(method = GET)
    public List<RaceRecord> getAllRaceRecords() {
        return raceRecordRepository.findAll();
    }

    @RequestMapping(method = POST)
    public void createRaceRecord(@RequestBody RaceRecord raceRecord) {
        raceRecordRepository.save(raceRecord);
    }

    @RequestMapping(path = "/{id}", method = GET)
    public RaceRecord getRaceRecordById(@PathVariable("id") String id) {
        validateRaceRecord(id);
        return raceRecordRepository.findById(id).get();
    }

    private void validateRaceRecord(String id) {
        if (!raceRecordRepository.findById(id).isPresent())
            throw new RaceRecordNotFoundException(id);
    }

    @RequestMapping(path = "{id}", method = DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteRaceRecordById(@PathVariable("id") String id) {
        validateRaceRecord(id);
        raceRecordRepository.deleteById(id);
    }

}
