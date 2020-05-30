package com.inci.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PaginationDto {
    private Long totalIngredients;
    private Integer totalPages;
    private Integer page;
    private Integer perPage;
    private List<InciDto> ingredients;
}
