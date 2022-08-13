package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class ResourceConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/storage/**")
                // for Windows System, use "file:///", for Unix System, use "file:/"
                .addResourceLocations("file:/" +
                        Paths.get("./storage/").toAbsolutePath().normalize().toString()
                                .replace("\\", "/") + "/");
    }
}