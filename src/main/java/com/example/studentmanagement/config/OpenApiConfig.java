package com.example.studentmanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI metadata rendered by Swagger UI.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI studentManagementOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Student Management API")
                .description("REST API built with Spring Boot, Spring Data JPA and documented with springdoc-openapi")
                .version("1.0.0")
                .contact(new Contact().name("MLIAEdu - J2E TP6"))
                .license(new License().name("MIT")));
    }
}
