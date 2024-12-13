package com.clara.discog.dto;

import lombok.Data;

@Data
public class ArtistResponseDto {
    private Integer idArtist;
    private String nameArtist;
    private int countReleases;
    private String activeYears;
    private String mostCommonTag;
    private String mostCommonGenre;
}
