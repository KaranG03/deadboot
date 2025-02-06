package com.deadStock.callFeature.devsoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.deadStock.callFeature.devsoc")

public class DevsocApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevsocApplication.class, args);
	}

}
