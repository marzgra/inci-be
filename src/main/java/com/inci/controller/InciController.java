package com.inci.controller;

import com.inci.dto.InciDto;
import com.inci.service.InciService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InciController {

    private final InciService inciService;

    public InciController(final InciService inciService) {
        this.inciService = inciService;
    }

    @GetMapping("/name")
    public InciDto getOne(@RequestParam String name) {
        return inciService.findByName(name);
    }
}
