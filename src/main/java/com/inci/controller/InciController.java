package com.inci.controller;

import com.inci.dto.InciDto;
import com.inci.dto.PaginationDto;
import com.inci.dto.SearchDto;
import com.inci.service.InciService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static java.util.stream.Collectors.toList;

@CrossOrigin
@RestController
public class InciController {

    private final InciService inciService;

    public InciController(final InciService inciService) {
        this.inciService = inciService;
    }

    @GetMapping("/ingredients/name/{name}")
    public ResponseEntity<InciDto> getByName(@PathVariable String name) {
        return ResponseEntity.ok(inciService.findByName(name));
    }

    @GetMapping("/analyze")
    public ResponseEntity<List<InciDto>> analyze(@RequestParam List<String> list) {
        return ResponseEntity.ok(
                list.stream()
                        .map(inciService::findByName)
                        .collect(toList()));
    }

    @PostMapping("/add")
    public ResponseEntity<Long> addNewIngredient(@RequestBody InciDto inciDto) throws URISyntaxException {
        return ResponseEntity.created(new URI(inciService.create(inciDto).toString())).body(inciService.create(inciDto));
    }

    @GetMapping("/ingredients")
    public ResponseEntity<PaginationDto> getAll(@RequestParam Integer page, @RequestParam Integer perPage) {
        return ResponseEntity.ok(inciService.getAll(page, perPage));
    }

    @GetMapping("/ingredients/id/{id}")
    public ResponseEntity<InciDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(inciService.getById(id));
    }

    @GetMapping("/ingredients/search/{part}")
    public ResponseEntity<List<SearchDto>> getById(@PathVariable String part) {
        return ResponseEntity.ok(inciService.search(part));
    }

}
