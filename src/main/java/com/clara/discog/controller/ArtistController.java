package com.clara.discog.controller;

import com.clara.discog.dto.ArtistDto;
import com.clara.discog.dto.ComparisonRequestDto;
import com.clara.discog.dto.ComparisonResponseDto;
import com.clara.discog.service.ArtistServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    private final ArtistServiceImpl artistService;

    public ArtistController(ArtistServiceImpl artistService) {
        this.artistService = artistService;
    }

    @GetMapping("/search-artist")
    public ResponseEntity<List<ArtistDto>> searchArtist(@RequestParam String name) {
        List<ArtistDto> artists = artistService.searchArtistByName(name);
        return ResponseEntity.ok(artists);
    }

    @PostMapping("/comparison")
    public ResponseEntity<ComparisonResponseDto> comparisonArtists(@RequestBody ComparisonRequestDto request) {
        return ResponseEntity.ok(artistService.getComparisonArtist(request));
    }

}
