package com.practice.bookmanageservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalApiService {

    private final WebClient openLibraryWebClient;

    public Mono<String> fetchBookInfoAsync(String title) {
        return openLibraryWebClient.get()
                .uri("/search.json?title={title}&limit=1", title)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(5))
                .doOnSuccess(r -> log.info("Fetched external data for: {}", title))
                .doOnError(e -> log.error("Failed to fetch: {}", e.getMessage()))
                .onErrorReturn("{}");
    }

    public String fetchBookInfoSync(String title){
        return fetchBookInfoAsync(title).block();
    }
}
