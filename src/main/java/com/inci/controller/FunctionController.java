package com.inci.controller;

import com.inci.dto.FunctionDto;
import com.inci.service.FunctionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FunctionController {

    private final FunctionService functionService;

    public FunctionController(final FunctionService functionService) {
        this.functionService = functionService;
    }

    @PostMapping("/functions/add")
    public Long addFunction(@RequestBody String functionName) {
        return functionService.addNewFunction(functionName);
    }

    @GetMapping("/functions/all")
    public List<FunctionDto> getAllFunctions() {
        return functionService.getAllFunctions();
    }
}
