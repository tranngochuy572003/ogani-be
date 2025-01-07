package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:63342/FoodMart-1.0.0/FoodMart-1.0.0/index.html?_ijt=ukvj63u85v8ng5v1d52jhf3r8v&_ij_reload=RELOAD_ON_SAVE") // Địa chỉ frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}

