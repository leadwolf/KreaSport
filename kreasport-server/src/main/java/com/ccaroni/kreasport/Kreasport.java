package com.ccaroni.kreasport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by Master on 11/06/2017.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.ccaroni.kreasport", "com.auth0.spring.security.mvc"})
@EnableMongoRepositories(basePackages = "com.ccaroni.kreasport.repository")
@EnableAutoConfiguration
@PropertySources({
        @PropertySource("classpath:application.properties"),
        @PropertySource("classpath:auth0.properties")
})
public class Kreasport {

    public static void main(final String[] args) throws Exception {
        SpringApplication.run(Kreasport.class, args);
    }
}
