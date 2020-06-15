package com.inci.controller;

import com.inci.dto.FunctionDto;
import com.inci.service.FunctionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class FunctionController {

    private final FunctionService functionService;

    public FunctionController(final FunctionService functionService) {
        this.functionService = functionService;
    }

    @PostMapping("/functions/add")
    public Long addFunction(@RequestBody FunctionDto function) {
        return functionService.addNewFunction(function);
    }

    @GetMapping("/functions/all")
    public List<FunctionDto> getAllFunctions() {
        return functionService.getAllFunctions();
    }
}
