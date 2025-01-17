// package com.example.demo.config;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.lang.NonNull;
// import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
// import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration
// public class WebConfig implements WebMvcConfigurer {

//     @Override
//     public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
//         registry.addResourceHandler("/**")
//                .addResourceLocations("classpath:/static/")
//                .setCachePeriod(3600);
//     }

//     @Override
//     public void addViewControllers(@NonNull ViewControllerRegistry registry) {
//         // Simplified routing for SPA
//         registry.addViewController("/").setViewName("forward:/index.html");
//         registry.addViewController("/**").setViewName("forward:/index.html");
//     }
// } 