package com.inci.controller;

import com.inci.dto.AnalyzeResultDto;
import com.inci.dto.InciDto;
import com.inci.dto.PaginationDto;
import com.inci.dto.SearchDto;
import com.inci.service.InciService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static java.util.stream.Collectors.toList;

@CrossOrigin
@RestController
@AllArgsConstructor
public class InciController {

    private final InciService inciService;

    @GetMapping("/ingredients/name/{name}")
    public ResponseEntity<InciDto> getByName(@PathVariable String name) {
        return ResponseEntity.ok(inciService.findByName(name));
    }

    @GetMapping("/analyze")
    public AnalyzeResultDto analyze(@RequestParam List<String> list) {
        return inciService.analyze(list);
    }

    @PostMapping("/ingredients/add")
    public ResponseEntity<Long> addNewIngredient(@RequestBody InciDto inciDto) {
        return ResponseEntity.ok(inciService.create(inciDto));
    }

    @PutMapping("/ingredients/edit")
    public ResponseEntity<Long> editIngredient(@RequestBody InciDto inciDto) {
        return ResponseEntity.ok(inciService.editInci(inciDto));
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

    @GetMapping("/proteins")
    public List<InciDto> addProteins() {
        return inciService.addProteins();
    }


}
