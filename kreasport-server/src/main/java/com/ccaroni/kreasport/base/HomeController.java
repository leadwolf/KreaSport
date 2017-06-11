package com.ccaroni.kreasport.base;

import com.auth0.Auth0User;
import com.auth0.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

@Controller
public class HomeController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value="/portal/home", method = RequestMethod.GET)
    protected String home(final Map<String, Object> model, final HttpServletRequest req, final Principal principal) {
        logger.info("Home page");
        final String name = principal.getName();
        logger.info("Principal name: " + name);
        final Auth0User user = SessionUtils.getAuth0User(req);
        model.put("user", user);
        return "home";
    }

}
