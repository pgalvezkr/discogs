package com.clara.discog.controller;

import com.clara.discog.service.DiscogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DiscogController {

    private final DiscogService discogService;

    public DiscogController(DiscogService discogService) {
        this.discogService = discogService;
    }

    @GetMapping("/search-artist")
    public String searchArtist(@RequestParam String name) {
        return discogService.searchArtist(name);
    }
}
