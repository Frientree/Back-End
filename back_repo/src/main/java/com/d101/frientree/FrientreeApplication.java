package com.d101.frientree;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FrientreeApplication {

	public static void main(String[] args) {
//		SpringApplication.run(FrientreeApplication.class, args);
		SpringApplication app = new SpringApplication(FrientreeApplication.class);
		app.setAdditionalProfiles("development");
		app.run(args);
	}

}
