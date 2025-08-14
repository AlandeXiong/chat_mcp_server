package com.example.mcp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Campaign Journey MCP Server API")
                .version("1.0.0")
                .description("MCP Server for marketing campaign journey management using Spring AI")
                .contact(new Contact()
                    .name("Campaign Journey Team")
                    .email("support@campaignjourney.com")
                    .url("https://github.com/campaignjourney"))
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT")));
    }
}
