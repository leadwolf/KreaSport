package com.ccaroni;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.ccaroni.repository")
@EnableAutoConfiguration
@ComponentScan("com.ccaroni")
public class KreasportApplication {

	public static void main(String[] args) {
		SpringApplication.run(KreasportApplication.class, args);
	}
}
