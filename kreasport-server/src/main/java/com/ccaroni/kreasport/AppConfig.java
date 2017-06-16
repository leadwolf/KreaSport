package com.ccaroni.kreasport;

import com.auth0.spring.security.mvc.Auth0Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * Created by Master on 11/06/2017.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class AppConfig extends Auth0Config {

    @Value(value = "${auth0.audience}")
    private String apiAudience;
    @Value(value = "${auth0.issuer}")
    private String issuer;


    @Override
    protected void authorizeRequests(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/css/**", "/fonts/**", "/js/**", "/login", "/dummy").permitAll()
//                .antMatchers("/portal/**").hasAuthority("ROLE_ADMIN")
                .antMatchers(securedRoute, "/map").authenticated();
    }
}
