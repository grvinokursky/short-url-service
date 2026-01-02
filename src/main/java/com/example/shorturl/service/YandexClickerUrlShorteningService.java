package com.example.shorturl.service;

import com.example.shorturl.config.WebClientConfig;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@Service
public class YandexClickerUrlShorteningService {

    private final WebClient webClient;

    public YandexClickerUrlShorteningService(WebClientConfig webClientConfig) {
        this.webClient = webClientConfig.clckWebClient(WebClient.builder());
    }

    public CompletableFuture<String> getShortUrlAsync(String url) {
        if (url == null || url.isBlank()) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("url is null or empty"));
        }

        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/--").queryParam("url", url).build())
                .accept(MediaType.TEXT_PLAIN)
                .retrieve()
                .onStatus(status -> !status.is2xxSuccessful(),
                        resp -> resp.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .flatMap(body -> Mono.error(new IOException(
                                        "Unexpected response: status=" + resp.statusCode() + ", body=" + body))))
                .bodyToMono(String.class)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .switchIfEmpty(Mono.error(new IOException("Empty response from shortening service")))
                .timeout(Duration.ofSeconds(10))
                .toFuture();
    }
}