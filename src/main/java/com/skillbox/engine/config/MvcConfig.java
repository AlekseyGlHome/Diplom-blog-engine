package com.skillbox.engine.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Value("${config.endOfFilePath}")
    private String endOfFilePath;

    @Value("${config.startOfFilePath}")
    private String startOfFilePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/edit/**", "/post/**")
                .addResourceLocations(startOfFilePath + endOfFilePath + "/");
    }

}
