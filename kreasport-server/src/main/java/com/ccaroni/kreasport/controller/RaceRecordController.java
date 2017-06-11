package com.ccaroni.kreasport.controller;

import com.ccaroni.kreasport.domain.RaceRecord;
import com.ccaroni.kreasport.exception.RaceRecordNotFoundException;
import com.ccaroni.kreasport.repository.RaceRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by Master on 30/05/2017.
 */
@RestController
@RequestMapping("/records")
public class RaceRecordController {

    @Autowired
    private RaceRecordRepository raceRecordRepository;

    @RequestMapping(method = GET)
    public List<RaceRecord> getAllRaceRecords() {
        return raceRecordRepository.findAll();
    }

    @RequestMapping(method = POST)
    public ResponseEntity<?> createRaceRecord(@RequestBody RaceRecord raceRecord) {
        raceRecordRepository.save(raceRecord);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(raceRecord.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @RequestMapping(path = "/{id}", method = GET)
    public RaceRecord getRaceRecordById(@PathVariable("id") String id) {
        validateRaceRecord(id);
        return raceRecordRepository.findById(id);
    }

    @RequestMapping(path = "{id}", method = DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteRaceRecordById(@PathVariable("id") String id) {
        validateRaceRecord(id);
        raceRecordRepository.deleteById(id);
    }


    @RequestMapping(path = "batch", method = DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteBatchRecords(@RequestBody List<String> idsToDelete) {
        for (String id : idsToDelete) {
            validateRaceRecord(id);
            raceRecordRepository.deleteById(id);
        }
    }

    @RequestMapping(path = "batch", method = POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public void saveBatchRecords(@RequestBody List<RaceRecord> recordList) {
        for (RaceRecord raceRecord : recordList) {
            raceRecordRepository.save(raceRecord);
        }
    }


    private void validateRaceRecord(String id) {
        if (raceRecordRepository.findById(id) == null)
            throw new RaceRecordNotFoundException(id);
    }


}
