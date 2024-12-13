package com.clara.discog.service;

import com.clara.discog.dto.*;
import com.clara.discog.dto.domain.DiscogsArtistResponse;
import com.clara.discog.exception.ArtistNotFoundException;
import com.clara.discog.exception.ExternalApiException;
import com.clara.discog.repository.GenreRepository;
import com.clara.discog.repository.ReleaseRepository;
import com.clara.discog.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArtistServiceImpl implements ArtistService {

    private final WebClient webClient;
    private final ReleaseRepository releaseRepository;
    private final TagRepository tagRepository;
    private final GenreRepository genreRepository;

    public ArtistServiceImpl(WebClient webClient, ReleaseRepository releaseRepository, TagRepository tagRepository, GenreRepository genreRepository) {
        this.webClient = webClient;
        this.releaseRepository = releaseRepository;
        this.tagRepository = tagRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public List<ArtistDto> searchArtistByName(String artistName) {
        List<ArtistDto> artistas = new ArrayList<>();
        String response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/database/search")
                        .queryParam("q", artistName)
                        .queryParam("type", "artist")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            DiscogsArtistResponse discogsResponse = DiscogsArtistResponse.fromJson(response);

            artistas = discogsResponse.getResults().stream()
                    .map(result -> {
                        ArtistDto artista = new ArtistDto();
                        artista.setName(result.getTitle());
                        artista.setDiscogId(result.getId());
                        artista.setId(result.getId());
                        return artista;
                    })
                    .toList();
        } catch (WebClientResponseException.NotFound ex) {
            throw new ArtistNotFoundException("Artist not found with name : " + artistName);
        } catch (Exception ex) {
            throw new ExternalApiException("Discogs has an error ", ex);
        }
        return artistas;
    }

    @Override
    public ComparisonResponseDto getComparisonArtist(ComparisonRequestDto comparisonRequestDto) {
        ComparisonResponseDto comparisonResponseDto = new ComparisonResponseDto();
        comparisonResponseDto.setArtistResponseDtos(new ArrayList<>());
        List<CriteriaType> criteriaTypes = comparisonRequestDto.getCriteriaTypes();
        List<Integer> artists = comparisonRequestDto.getArtistIds();
        if (criteriaTypes.isEmpty()) {
            addAllComparisonsForArtists(artists, comparisonResponseDto.getArtistResponseDtos());
        } else {
            addComparisonsForArtistsAndCriteria(criteriaTypes, artists, comparisonResponseDto.getArtistResponseDtos());
        }

        return comparisonResponseDto;
    }

    private void addAllComparisonsForArtists(List<Integer> artists, List<ArtistResponseDto> artistResponses) {
        artists.forEach(artistId -> artistResponses.add(getAllComparisons(artistId)));
    }

    private void addComparisonsForArtistsAndCriteria(List<CriteriaType> criteriaTypes, List<Integer> artists, List<ArtistResponseDto> artistResponses) {
        for (Integer artistId : artists) {
            ArtistResponseDto artistResponseDto = new ArtistResponseDto();
            for (CriteriaType criteriaType : criteriaTypes) {
                getResponseByCriteriaType(criteriaType, artistId, artistResponseDto);
            }
            artistResponses.add(artistResponseDto);
        }
    }

    private void getResponseByCriteriaType(CriteriaType criteriaType, Integer artistId, ArtistResponseDto artistResponseDto) {
        artistResponseDto.setIdArtist(artistId);
        switch (criteriaType) {
            case RELEASES -> artistResponseDto.setCountReleases(getReleasesByArtist(artistId));
            case ACTIVE_YEARS -> artistResponseDto.setActiveYears(getActiveYearsOfArtist(artistId));
            case TAG -> artistResponseDto.setMostCommonTag(getMostPopularTagByArtist(artistId));
            case GENRE -> artistResponseDto.setMostCommonGenre(getMostCommonGenreByArtist(artistId));
        }
    }

    private ArtistResponseDto getAllComparisons(Integer artistId) {
        ArtistResponseDto artistResponseDto = new ArtistResponseDto();
        artistResponseDto.setIdArtist(artistId);
        artistResponseDto.setCountReleases(getReleasesByArtist(artistId));
        artistResponseDto.setActiveYears(getActiveYearsOfArtist(artistId));
        artistResponseDto.setMostCommonGenre(getMostCommonGenreByArtist(artistId));
        artistResponseDto.setMostCommonTag(getMostPopularTagByArtist(artistId));
        return artistResponseDto;
    }

    private int getReleasesByArtist(Integer artistId) {
        return releaseRepository.countReleasesByArtistId(artistId);
    }

    private String getActiveYearsOfArtist(Integer artistId) {
        Integer firstYear = releaseRepository.findFirstReleaseYearByArtistId(artistId);
        Integer lastYear = releaseRepository.findLastReleaseYearByArtistId(artistId);
        return String.valueOf(lastYear - firstYear);
    }

    private String getMostCommonGenreByArtist(Integer artistId) {
        return genreRepository.findMostCommonGenresByArtistId(artistId).get(0).getName();
    }

    private String getMostPopularTagByArtist(Integer artistId) {
        return tagRepository.findMostCommonTagsByArtistId(artistId).get(0).getName();
    }
}
