package com.storage.manager.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI config() {
        return new OpenAPI().info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .version("1.0")
                .description("Storage Manager Server")
                .description("Serviço para gerenciamento de arquivos em multicloud")
                .contact(new Contact().name("Jeanluca F Pereira").url("").email("jeanlucafp@gmail.com"));
    }
}
