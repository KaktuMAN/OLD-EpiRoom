package com.epiroom.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

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
                .description("This API is a wrapper around the Epitech Intranet API. (Mainly used for EpiRoom)")
                .license(mitLicense);

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("oauth2");

        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement);
    }
}