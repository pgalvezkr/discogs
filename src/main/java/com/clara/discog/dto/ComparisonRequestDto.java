package com.clara.discog.dto;

import lombok.Data;

import java.util.List;

@Data
public class ComparisonRequestDto {
    List <Integer> artistIds;
    List <CriteriaType> criteriaTypes;
}
