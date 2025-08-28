package com.uasz.evaluation.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Évaluation Étudiants - UASZ")
                        .version("1.1.0")
                        .description("Cette API permet de gérer les évaluations des étudiants inscrits à l'Université Assane Seck de Ziguinchor (UASZ). " +
                                "Elle inclut également la gestion des inscriptions, des utilisateurs et des imports/exports de données.")
                        .contact(new Contact().name("UASZ").url("https://www.uasz.sn").email("contact@uasz.sn"))
                        .license(new License().name("API License").url("https://www.uasz.sn/api-license")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()));
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .name("Authorization")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
    }
}