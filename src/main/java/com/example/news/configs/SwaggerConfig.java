package com.example.news.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(title = "IP-position", version = "0.3.0", license = @License(name = "MIT License", url = "https://github.com/Danila2608/ip-position/blob/main/LICENSE.md"), contact = @Contact(name = "Danila", url = "https://t.me/IVCTOPIVK", email = "danilakazakovmax@gmail.com")), servers = {
        @Server(url = "http://localhost:8080", description = "Local Server")
})

@Configuration
public class SwaggerConfig implements WebMvcConfigurer {

@Override
public void addViewControllers(@NonNull ViewControllerRegistry registry) {
registry.addRedirectViewController("/custom-path", "/swagger-ui.html");
    }
}
