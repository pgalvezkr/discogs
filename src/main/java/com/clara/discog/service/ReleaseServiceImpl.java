package com.clara.discog.service;

import com.clara.discog.dto.ArtistDto;
import com.clara.discog.dto.GenreDto;
import com.clara.discog.dto.ReleaseDto;
import com.clara.discog.dto.TagDto;
import com.clara.discog.dto.domain.DiscogsResponse;
import com.clara.discog.entity.Artist;
import com.clara.discog.entity.Genre;
import com.clara.discog.entity.Release;
import com.clara.discog.entity.Tag;
import com.clara.discog.exception.ArtistNotFoundException;
import com.clara.discog.exception.ExternalApiException;
import com.clara.discog.repository.ArtistRepository;
import com.clara.discog.repository.GenreRepository;
import com.clara.discog.repository.ReleaseRepository;
import com.clara.discog.repository.TagRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReleaseServiceImpl implements ReleaseService {

    private final WebClient webClient;
    private final ReleaseRepository releaseRepository;
    private final ArtistRepository artistRepository;
    private final GenreRepository genreRepository;
    private final TagRepository tagRepository;

    public ReleaseServiceImpl(WebClient webClient, ReleaseRepository releaseRepository, ArtistRepository artistRepository, GenreRepository genreRepository, TagRepository tagRepository) {
        this.webClient = webClient;
        this.releaseRepository = releaseRepository;
        this.artistRepository = artistRepository;
        this.genreRepository = genreRepository;
        this.tagRepository = tagRepository;
    }

    public List<ReleaseDto> getDiscography(ArtistDto artistDto) {
        List<ReleaseDto> discography = new ArrayList<>();

        String response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/database/search")
                        .queryParam("artist", artistDto.getName())
                        .queryParam("type", "release")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            DiscogsResponse discogsResponse = DiscogsResponse.fromJson(response);

            discography = discogsResponse.getResults().stream()
                    .map(result -> {
                        ReleaseDto release = new ReleaseDto();
                        release.setReleaseYear(result.getYear());
                        release.setTitle(result.getTitle());
                        //Recupero tags
                        release.setTags(result.getLabel().stream().map(tag -> {
                            TagDto tagDto = new TagDto();
                            tagDto.setName(tag);
                            return tagDto;
                        }).toList());
                        //Recupero generos
                        release.setGenres(result.getGenre().stream().map(genre->{
                            GenreDto genreDto = new GenreDto();
                            genreDto.setName(genre);
                            return genreDto;
                        }).toList());
                        return release;
                    })
                    .toList();
        } catch (WebClientResponseException.NotFound ex) {
            throw new ArtistNotFoundException("Artist not found" );
        } catch (Exception ex) {
            throw new ExternalApiException("Discogs has an error ", ex);
        }
        return discography;
    }

    @Override
    @Transactional
    public String saveDiscography(ArtistDto artistDto) throws Exception {
        //1. Get the discography from discogAPI

        List<ReleaseDto> discography = getDiscography(artistDto);
        Integer artistId = null;
        try{
        //2. Save artist
        Artist artist = new Artist();
        artist.setDiscogsId(artistDto.getDiscogId());
        artist.setName(artistDto.getName());
        Artist artisSaved = artistRepository.save(artist);

        //3. Save releases
        discography.forEach(release -> {
            Release releaseSaved = saveRelease(release, artisSaved);
            //4. Save genres
            release.getGenres().forEach(genre -> {
                Genre genreEntity = new Genre();
                genreEntity.setName(genre.getName());
                genreEntity.setRelease(releaseSaved);
                genreRepository.save(genreEntity);
            });
            //5. Save tags
            release.getTags().forEach(tag -> {
                Tag tagEntity = new Tag();
                tagEntity.setName(tag.getName());
                tagEntity.setRelease(releaseSaved);
                tagRepository.save(tagEntity);
            });
        });
        artistId = artisSaved.getId();
        }catch (DataIntegrityViolationException ex) {
            throw new ArtistNotFoundException("The artist you try to get discography is alreday in our database");
        }
        return "The discography has been saved and the id for the artist is " + artistId;
    }


    @Transactional
    public Release saveRelease(ReleaseDto release, Artist artist) {
        Release releaseEntity = new Release();
        releaseEntity.setReleaseYear(release.getReleaseYear());
        releaseEntity.setTitle(release.getTitle());
        releaseEntity.setArtist(artist);
        return releaseRepository.save(releaseEntity);
    }
}