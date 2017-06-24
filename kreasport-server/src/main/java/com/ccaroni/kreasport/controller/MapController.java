package com.ccaroni.kreasport.controller;

import java.security.Principal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.auth0.Auth0User;
import com.auth0.SessionUtils;
import com.ccaroni.kreasport.AppConfig;

/**
 * Created by Master on 16/06/2017.
 */
@Controller
public class MapController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private AppConfig appConfig;
    
    @Autowired
    public MapController(final AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @RequestMapping("/map")
    public String base(final Map<String, Object> model, final HttpServletRequest req, final Principal principal) {
        logger.debug("Getting map");
        logger.debug("clientId : "+appConfig.getClientId());
        final String name = principal.getName();
        logger.info("Principal name: " + name);
        final Auth0User user = SessionUtils.getAuth0User(req);
        model.put("user", user);
        logger.info(user.getUserId());
        return "map";
    }
    

}
