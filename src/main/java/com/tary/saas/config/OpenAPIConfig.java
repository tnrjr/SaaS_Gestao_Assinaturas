package com.tary.saas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SaaS API")
                        .description("API para gestão de assinaturas, planos e faturas")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Seu Nome")
                                .email("seuemail@empresa.com"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
