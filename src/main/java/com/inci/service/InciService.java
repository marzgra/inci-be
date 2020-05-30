package com.inci.service;

import com.inci.dto.InciDto;
import com.inci.dto.PaginationDto;
import com.inci.dto.SearchDto;
import com.inci.entity.Inci;
import com.inci.mapper.Mapper;
import com.inci.repository.AlternativeNameRepository;
import com.inci.repository.InciRepository;
import com.inci.repository.PolishNameRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.*;

@Service
public class InciService {

    private final InciRepository inciRepository;
    private final AlternativeNameRepository alternativeNameRepository;
    private final PolishNameRepository polishNameRepository;
    private final Mapper mapper;
    private final LevenshteinDistanceService levenshteinDistanceService;

    public InciService(final InciRepository inciRepository,
                       final AlternativeNameRepository alternativeNameRepository,
                       final PolishNameRepository polishNameRepository,
                       final Mapper mapper, LevenshteinDistanceService levenshteinDistanceService) {
        this.inciRepository = inciRepository;
        this.alternativeNameRepository = alternativeNameRepository;
        this.polishNameRepository = polishNameRepository;
        this.mapper = mapper;
        this.levenshteinDistanceService = levenshteinDistanceService;
    }

    public InciDto findByName(String name) {
        return inciRepository.findByInciName(name)
                .map(inci -> mapper.map(inci, InciDto.class))
                .orElse(new InciDto("not found: " + name));
    }

    @Transactional
    public Long create(InciDto inciDto) {
        return inciRepository.findByInciName(inciDto.getInciName())
                .map(Inci::getInciId)
                .orElse(createNewInci(inciDto));
    }

    private Long createNewInci(InciDto inciDto) {
        Inci newInci = mapper.map(inciDto, Inci.class);
        newInci.getAlternativeNames().forEach(alt -> alt.setInci(newInci));
        newInci.getPolishNames().forEach(pl -> pl.setInci(newInci));
        return inciRepository.save(newInci).getInciId();
    }

    public PaginationDto getAll(Integer page, Integer perPage) {
        Long numberOfIngredients = inciRepository.numberOfEntries();
        PaginationDto pageDto = new PaginationDto();
        pageDto.setPage(page);
        pageDto.setPerPage(perPage);
        pageDto.setTotalIngredients(numberOfIngredients);
        pageDto.setTotalPages(Long.valueOf(numberOfIngredients / perPage).intValue());

        pageDto.setIngredients(inciRepository.findAll(PageRequest.of(page, perPage))
                .stream()
                .map(inci -> mapper.map(inci, InciDto.class))
                .collect(Collectors.toList()));

        return pageDto;
    }

    public List<SearchDto> search(String part) {
        List<SearchDto> result = alternativeNameRepository.findAllByAlternativeNameContaining(part.toLowerCase())
                .stream()
                .map(inci -> mapper.map(inci, SearchDto.class)).collect(Collectors.toList());

        result.addAll(polishNameRepository.findAllByPolishNameContaining(part.toLowerCase())
                .stream()
                .map(inci -> mapper.map(inci, SearchDto.class))
                .collect(Collectors.toList()));

        result.addAll(inciRepository.findAllByInciNameContaining(part.toLowerCase())
                .stream()
                .map(inci -> mapper.map(inci, SearchDto.class))
                .collect(Collectors.toList()));

        result.forEach(dto -> dto.setSimilarity(levenshteinDistanceService.calculateSimilarity(part.toLowerCase(), dto.getName())));

        result.sort(comparing(SearchDto::getSimilarity).reversed());

        return result;
    }

    public InciDto getById(Long id) {
        return inciRepository.findById(id)
                .map(inci -> mapper.map(inci, InciDto.class))
                .orElse(new InciDto("not found: " + id));
    }
}
