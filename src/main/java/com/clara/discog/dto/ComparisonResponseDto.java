package com.clara.discog.dto;

import lombok.Data;

import java.util.List;

@Data
public class ComparisonResponseDto {
    List<ArtistResponseDto> artistResponseDtos;
}
