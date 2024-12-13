package com.clara.discog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class MostCriteria {
    private String artistName;
    private long count;
    private String name;
}
