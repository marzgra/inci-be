package com.inci.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class InciDto {

    private String inciName;

    private String description;

    private List<String> functionNames = new ArrayList<>();

    private List<String> polishNames = new ArrayList<>();

    private List<String> alternativeNames = new ArrayList<>();

}