//package com.example.Bright_Aid.Security;
//
//import io.swagger.v3.oas.models.Components;
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Contact;
//import io.swagger.v3.oas.models.info.Info;
//import io.swagger.v3.oas.models.security.SecurityRequirement;
//import io.swagger.v3.oas.models.security.SecurityScheme;
//import io.swagger.v3.oas.models.servers.Server;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.List;
//
//@Configuration
//public class SwaggerConfig {
//
//    @Value("${server.port:8080}")
//    private String serverPort;
//
//    @Bean
//    public OpenAPI customOpenAPI() {
//        final String securitySchemeName = "bearerAuth";
//
//        return new OpenAPI()
//                .info(new Info()
//                        .title("Bright Aid API")
//                        .description("API documentation for Bright Aid application")
//                        .version("1.0.0")
//                        .contact(new Contact()
//                                .name("Bright Aid Team")
//                                .email("support@brightaid.com")))
//                .servers(List.of(
//                        new Server()
//                                .url("http://localhost:" + serverPort)
//                                .description("Local Development Server")
//                ))
//                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
//                .components(new Components()
//                        .addSecuritySchemes(securitySchemeName,
//                                new SecurityScheme()
//                                        .name(securitySchemeName)
//                                        .type(SecurityScheme.Type.HTTP)
//                                        .scheme("bearer")
//                                        .bearerFormat("JWT")
//                                        .description("Enter JWT token (without 'Bearer ' prefix)")
//                        )
//                );
//    }
//}