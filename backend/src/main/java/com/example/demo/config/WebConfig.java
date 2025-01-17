package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // Handle all static resources including themes
        registry.addResourceHandler("/**")
               .addResourceLocations("classpath:/static/")
               .setCachePeriod(3600);
    }

    @Override
    public void addViewControllers(@NonNull ViewControllerRegistry registry) {
        // Forward non-resource requests to React Router
        registry.addViewController("/")
               .setViewName("forward:/index.html");
        registry.addViewController("/**/{path:[^\\.]*}")
               .setViewName("forward:/index.html");
    }
} 