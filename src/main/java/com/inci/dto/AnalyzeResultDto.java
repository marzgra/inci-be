package com.inci.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AnalyzeResultDto {
    private String type;
    private List<InciDto> ingredients;
}
