package com.clara.discog.service;

import com.clara.discog.dto.ArtistDto;
import com.clara.discog.dto.ComparisonRequestDto;
import com.clara.discog.dto.ComparisonResponseDto;

import java.util.List;

public interface ArtistService {
    List<ArtistDto> searchArtistByName(String artistName);
    ComparisonResponseDto getComparisonArtist(ComparisonRequestDto comparisonRequestDto);
}
