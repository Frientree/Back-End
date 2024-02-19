package com.d101.frientree.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@OpenAPIDefinition(
        info = @Info(title = "프렌트리 api",
                description = "프렌트리 api",
                version = "v1"
        )
)
@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("user-api")
                .pathsToMatch("/users/**")
                .build();
    }

    @Bean
    public GroupedOpenApi leafApi() {
        return GroupedOpenApi.builder()
                .group("leaf-api")
                .pathsToMatch("/leaf/**")
                .build();
    }

    @Bean
    public GroupedOpenApi userFruitApi(){
        return GroupedOpenApi.builder()
                .group("userFruit-api")
                .pathsToMatch("/user-fruit/**")
                .build();
    }

    @Bean
    public GroupedOpenApi juiceApi(){
        return GroupedOpenApi.builder()
                .group("juice-api")
                .pathsToMatch("/juice/**")
                .build();
    }

    @Bean
    public GroupedOpenApi calendarApi(){
        return GroupedOpenApi.builder()
                .group("calendar-api")
                .pathsToMatch("/calendar/**")
                .build();
    }

    @Bean
    public GroupedOpenApi TermsApi(){
        return GroupedOpenApi.builder()
                .group("terms-api")
                .pathsToMatch("/terms/**")
                .build();
    }

    @Bean
    public GroupedOpenApi appVersionApi(){
        return GroupedOpenApi.builder()
                .group("app-version")
                .pathsToMatch("/app-version/**")
                .build();
    }

    @Bean
    public GroupedOpenApi messageApi(){
        return GroupedOpenApi.builder()
                .group("message")
                .pathsToMatch("/message/**")
                .build();
    }


    @Bean
    public OpenAPI openAPI(){
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER).name("Authorization");
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .security(Arrays.asList(securityRequirement));
    }

}
