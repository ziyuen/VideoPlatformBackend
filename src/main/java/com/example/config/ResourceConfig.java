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
                // for Windows system:
//                .addResourceHandler("/storage/**")
//                .addResourceLocations("file:/" +
//                        Paths.get("./storage/").toAbsolutePath().normalize().toString()
//                                .replace(" \\", "/") + "/");
                // for Unix system:
                .addResourceHandler("/storage/**")
                .addResourceLocations("file:" + Paths.get("./storage/").toAbsolutePath().normalize() + "/");
    }
}