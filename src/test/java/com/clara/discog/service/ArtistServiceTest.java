package com.clara.discog.service;

import com.clara.discog.dto.ComparisonRequestDto;
import com.clara.discog.dto.ComparisonResponseDto;
import com.clara.discog.dto.CriteriaType;
import com.clara.discog.dto.MostCriteria;
import com.clara.discog.repository.GenreRepository;
import com.clara.discog.repository.ReleaseRepository;
import com.clara.discog.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
public class ArtistServiceTest {

    @Mock
    private ReleaseRepository releaseRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private ArtistServiceImpl artistService;

    void setUpForExistArtist() {
        Mockito.lenient().when(releaseRepository.countReleasesByArtistId(any())).thenReturn(5);
        Mockito.lenient().when(releaseRepository.findLastReleaseYearByArtistId(any())).thenReturn(2000);
        Mockito.lenient().when(releaseRepository.findFirstReleaseYearByArtistId(any())).thenReturn(1990);
        Mockito.lenient().when(tagRepository.findMostCommonTagsByArtistId(1)).thenReturn(List.of(MostCriteria.builder()
                .artistName("Avicci")
                .count(35)
                .name("Rock Legends")
                .build()));
        Mockito.lenient().when(genreRepository.findMostCommonGenresByArtistId(1)).thenReturn(List.of(MostCriteria.builder()
                .artistName("Avicci")
                .count(36)
                .name("Rock")
                .build()));

        Mockito.lenient().when(tagRepository.findMostCommonTagsByArtistId(2)).thenReturn(List.of(MostCriteria.builder()
                .artistName("Quee")
                .count(35)
                .name("Rock & roll")
                .build()));
        Mockito.lenient().when(genreRepository.findMostCommonGenresByArtistId(2)).thenReturn(List.of(MostCriteria.builder()
                .artistName("Quee")
                .count(36)
                .name("Rock")
                .build()));

    }

    @Test
    void getComparisonArtist_whenDontHaveCriteria_returnAllCriteria()  {
        setUpForExistArtist();
        ComparisonRequestDto comparisonRequestDto = new ComparisonRequestDto();
        comparisonRequestDto.setCriteriaTypes(List.of());
        comparisonRequestDto.setArtistIds(List.of(1,2));
        ComparisonResponseDto response= artistService.getComparisonArtist(comparisonRequestDto);
        assertEquals(2, response.getArtistResponseDtos().size());
        response.getArtistResponseDtos().forEach(responseDto -> {
            assertNotNull(responseDto.getIdArtist());
            assertNotNull(responseDto.getActiveYears());
            assertNotNull(responseDto.getMostCommonTag());
            assertNotNull(responseDto.getMostCommonGenre());
            assertNotEquals(0, responseDto.getCountReleases());
        });
    }

    @Test
    void getComparisonArtist_whenHaveTwoCriteria_returnTheSelectedCriteria() {
        setUpForExistArtist();
        ComparisonRequestDto comparisonRequestDto = new ComparisonRequestDto();
        comparisonRequestDto.setCriteriaTypes(List.of(CriteriaType.GENRE, CriteriaType.TAG));
        comparisonRequestDto.setArtistIds(List.of(1,2));
        ComparisonResponseDto response= artistService.getComparisonArtist(comparisonRequestDto);
        assertEquals(2, response.getArtistResponseDtos().size());
        response.getArtistResponseDtos().forEach(responseDto -> {
            assertNotNull(responseDto.getIdArtist());
            assertNotNull(responseDto.getMostCommonTag());
            assertNotNull(responseDto.getMostCommonGenre());
        });
    }

}
