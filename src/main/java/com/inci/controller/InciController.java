package com.inci.controller;

import com.inci.dto.InciDto;
import com.inci.service.InciService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
public class InciController {

    private final InciService inciService;

    public InciController(final InciService inciService) {
        this.inciService = inciService;
    }

    @GetMapping("/name")
    public InciDto getByName(@RequestParam String name) {
        return inciService.findByName(name);
    }

    @GetMapping("/list")
    public List<InciDto> analyze(@RequestParam List<String> list) {
        return list
                .stream()
                .map(inciService::findByName)
                .collect(toList());
    }

    @PostMapping("/add")
    public Long addNewIngredient(@RequestBody InciDto inciDto) {
        return inciService.create(inciDto);
    }
}
