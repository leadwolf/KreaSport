package com.ccaroni.kreasport;

import com.ccaroni.kreasport.rest.auth.AuthFilter;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.TracingConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.glassfish.jersey.server.mvc.jsp.JspMvcFeature;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Master on 13/04/2017.
 */
public class RestApp extends ResourceConfig {

    public RestApp() {
        packages("com.ccaroni.kreasport.rest");

        // MVC.
        register(JspMvcFeature.class);

        // Logging.
        register(new LoggingFeature(Logger.getLogger(LoggingFeature.DEFAULT_LOGGER_NAME), Level.INFO, LoggingFeature.Verbosity.HEADERS_ONLY, Integer.MAX_VALUE));

        // Tracing support.
        property(ServerProperties.TRACING, TracingConfig.ON_DEMAND.name());

        register(AuthFilter.class);
        register(RolesAllowedDynamicFeature.class);
    }
}
