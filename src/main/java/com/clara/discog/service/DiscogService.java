package com.clara.discog.service;


import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class DiscogService {

    private final WebClient webClient;

    public DiscogService(WebClient webClient) {
        this.webClient = webClient;
    }

    public String searchArtist(String artistName) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/database/search")
                        .queryParam("q", artistName)
                        .queryParam("type", "artist")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
