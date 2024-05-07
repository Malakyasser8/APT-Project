package com.Backend.APTBackend.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@SuppressWarnings("null") CorsRegistry registry) {
        registry.addMapping("/**") // Allow CORS for all endpoints
                .allowedOrigins("http://localhost:3000") // Specify the origin allowed to access the resources
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Specify the HTTP methods allowed
                .allowedHeaders("*") // Specify the headers allowed in the request
                .exposedHeaders("Authorization");
    }
}