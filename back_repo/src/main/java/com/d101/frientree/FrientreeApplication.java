package com.d101.frientree;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@OpenAPIDefinition(
		servers = {
				@Server(url="/", description = "Default Server url")
		}
)
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class FrientreeApplication {

	public static void main(String[] args) {
//		SpringApplication.run(FrientreeApplication.class, args);
		SpringApplication app = new SpringApplication(FrientreeApplication.class);
//		app.setAdditionalProfiles("development");
		app.setAdditionalProfiles("test");
		app.run(args);
	}

}
