package com.example.shorturl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    private final String baseAddress;

    public WebClientConfig(ApplicationProperties applicationProperties) {
        baseAddress = applicationProperties.getConnectionStrings().getYandexClickerBaseAddress();
    }

    @Bean
    public WebClient clckWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(baseAddress)
                .build();
    }
}