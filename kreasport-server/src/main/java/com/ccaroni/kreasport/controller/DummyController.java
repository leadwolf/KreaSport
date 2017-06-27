package com.ccaroni.kreasport.controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ccaroni.kreasport.domain.Race;

/**
 * Created by Master on 16/06/2017.
 */
@RestController
public class DummyController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(path = "/dummy", method = POST)
        public void dummy(@RequestBody Race race) {
            logger.debug("DUMMY REQUEST");
            logger.debug("received Race: " + race);
            
    }


}
