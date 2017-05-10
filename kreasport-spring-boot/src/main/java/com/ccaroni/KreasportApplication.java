package com.ccaroni;

import com.ccaroni.domain.Account;
import com.ccaroni.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
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

	@Bean
	CommandLineRunner init(final AccountRepository accountRepository) {

		return new CommandLineRunner() {

			@Override
			public void run(String... arg0) throws Exception {
				accountRepository.save(new Account("rbaxter", "password"));

			}

		};

	}
}
