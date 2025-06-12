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
                .title("IAM API")
                .description("Identity And Access API")
                .version("1.0")
                .contact(new Contact()
                    .name("Incora")
                    .email("support@incora.com")
                    .url("https://incora.com"))
                .license(new License()
                    .name("Incora Commercial")
                    .url("https://www.incora.com")))
            .servers(List.of(
                new Server()
                    .url("/chemicals/api/iam")
                    .description("IAM API server")));
    }
}
