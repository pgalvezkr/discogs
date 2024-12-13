package com.clara.discog.dto;

import com.clara.discog.entity.Artist;
import com.clara.discog.entity.Tag;
import lombok.Data;

import java.util.List;

@Data
public class ReleaseDto {
    private Long id;
    private String title;
    private String releaseYear;
    private Artist artist;
    private List<TagDto> tags;
    private List<GenreDto> genres;
}
