package com.example.netologydiplom.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@EnableWebMvc
class WebConfig implements WebMvcConfigurer {
    @Value("${origins}")
    private String frontURL;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedOrigins(frontURL)
                .allowedMethods("*");
    }


}