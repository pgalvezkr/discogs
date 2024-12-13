package com.clara.discog.controller;

import com.clara.discog.dto.ArtistDto;
import com.clara.discog.service.ReleaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/release")
public class ReleaseController {

    private final ReleaseService releaseService;

    public ReleaseController(ReleaseService releaseService) {
        this.releaseService = releaseService;
    }

    @PostMapping("/generate")
    public ResponseEntity<String> searchArtist(@RequestBody ArtistDto artist) throws Exception {
        String result = releaseService.saveDiscography(artist);
        return ResponseEntity.ok(result);
    }
}
