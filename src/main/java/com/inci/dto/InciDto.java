package com.inci.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@NoArgsConstructor
public class InciDto {

    private Long inciId;

    private String inciName;

    private String description;

    private List<FunctionDto> functionNames = new ArrayList<>();

    private List<String> polishNames = new ArrayList<>();

    private List<String> alternativeNames = new ArrayList<>();

    public InciDto(String inciName) {
        this.inciName = inciName;
    }

    public void setInciName(String inciName) {
        this.inciName = inciName.toLowerCase();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFunctionNames(List<FunctionDto> functionNames) {
        this.functionNames = functionNames;
    }

    public void setPolishNames(List<String> polishNames) {
        this.polishNames = polishNames
                .stream()
                .map(String::toLowerCase)
                .collect(toList());
    }

    public void setAlternativeNames(List<String> alternativeNames) {
        this.alternativeNames = alternativeNames
                .stream()
                .map(String::toLowerCase)
                .collect(toList());
    }
}