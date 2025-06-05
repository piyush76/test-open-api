package com.userops.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI userOpsEntityOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("User Operations Entity API")
                .description("Identity And Access API for managing user operations entity assignments")
                .version("1.0")
                .contact(new Contact()
                    .name("UserOps API")
                    .email("support@userops.com")
                    .url("https://userops.com"))
                .license(new License()
                    .name("Commercial License")
                    .url("https://www.userops.com")))
            .servers(List.of(
                new Server()
                    .url("/")
                    .description("Local development server")));
    }
}
