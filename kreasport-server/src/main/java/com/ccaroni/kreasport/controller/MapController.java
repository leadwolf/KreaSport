package com.ccaroni.kreasport.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Master on 16/06/2017.
 */
@Controller
public class MapController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("/map")
    public String base() {
        logger.debug("Getting map");
        return "map";
    }

}
