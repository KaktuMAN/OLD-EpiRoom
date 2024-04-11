package com.epiroom.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class OpenAPIConfig {

    @Value("${environment.api-base-url}")
    private String API_URL;

    @Bean
    public OpenAPI myOpenAPI() {
        Contact contact = new Contact();
        contact.setEmail("nicolas.pechart@epitech.eu");
        contact.setName("Nicolas");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("EpiRoom API")
                .version("0.3")
                .contact(contact)
                .description("This API is used for EpiRoom")
                .license(mitLicense);

        Server server = new Server();
        server.setUrl(API_URL);

        return new OpenAPI()
            .info(info)
            .servers(Collections.singletonList(server));
    }
}