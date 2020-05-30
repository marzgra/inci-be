package com.inci.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SearchDto {
    private String name;
    private Long inciId;
    private double similarity;
}
