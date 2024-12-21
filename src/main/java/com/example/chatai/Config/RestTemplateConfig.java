package com.example.chatai.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {
    @Autowired
    private ChatModelConfig chatModelConfig;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofSeconds(chatModelConfig.getTimeout())) // Connection timeout
                .setReadTimeout(Duration.ofSeconds(chatModelConfig.getTimeout()))    // Read timeout
                .build();
    }

}
