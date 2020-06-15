package com.inci.service;

import com.inci.dto.InciDto;
import com.inci.dto.PaginationDto;
import com.inci.dto.SearchDto;
import com.inci.entity.AlternativeName;
import com.inci.entity.Function;
import com.inci.entity.Inci;
import com.inci.mapper.Mapper;
import com.inci.repository.AlternativeNameRepository;
import com.inci.repository.FunctionRepository;
import com.inci.repository.InciRepository;
import com.inci.repository.PolishNameRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

@Service
@AllArgsConstructor
public class InciService {

    private final Logger log = LoggerFactory.getLogger(InciService.class);

    private final InciRepository inciRepository;
    private final AlternativeNameRepository alternativeNameRepository;
    private final PolishNameRepository polishNameRepository;
    private final FunctionRepository functionRepository;
    private final Mapper mapper;
    private final LevenshteinDistanceService levenshteinDistanceService;

    public InciDto findByName(String name) {
        log.info("Finding INCI by name: " + name);
        return inciRepository.findByInciName(name)
                .map(inci -> mapper.map(inci, InciDto.class))
                .orElse(findAsAlternativeName(name));
    }

    private InciDto findAsAlternativeName(String name) {
        log.info("Find INCI as alternative name: " + name);
        return alternativeNameRepository.findByAlternativeName(name)
                .map(AlternativeName::getInci)
                .map(inci -> mapper.map(inci, InciDto.class))
                .orElse(new InciDto("not found: " + name));
    }
//    private InciDto containsOil(String name) {
//        if (name.toLowerCase().contains("oil")) {
//            return new InciDto(name, )
//        }
//    }
    // contains protein

    @Transactional
    public Long create(InciDto inciDto) {
        inciDto.setInciName(inciDto.getInciName().toLowerCase());
        log.info("Creating new INCI: " + inciDto.toString());
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
        log.info("Getting INCIs " + perPage + " per page, " + page + " page.");
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
        log.info("Looking for INCI with any name like: " + part);
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

        return result
                .stream()
                .filter(dto -> dto.getSimilarity() >= 0)
                .sorted(comparing(SearchDto::getSimilarity).reversed())
                .collect(Collectors.toList());
    }

    public InciDto getById(Long id) {
        log.info("Looking for INCI by id: " + id);
        return inciRepository.findById(id)
                .map(inci -> mapper.map(inci, InciDto.class))
                .orElse(new InciDto("not found: " + id));
    }

    public Long editInci(InciDto inciDto) {
        log.info("Update INCI with dto: " + inciDto.toString());
        Inci inciToSave = mapper.map(inciDto, Inci.class);
        inciToSave.getAlternativeNames().forEach(alt -> alt.setInci(inciToSave));
        inciToSave.getPolishNames().forEach(pl -> pl.setInci(inciToSave));
//        log.info("INCI to save after mapping: " + inciToSave.toString());
        Inci savedInci = inciRepository.save(inciToSave);
        return savedInci.getInciId();
    }

    public List<InciDto> addProteins() {
        Function fun = functionRepository.findByFunctionName("proteiny").get();
        return inciRepository.findAllByInciNameContaining("protein")
                .stream()
                .map(inci -> addFunction(inci, fun))
                .map(inci -> mapper.map(inci, InciDto.class))
                .collect(Collectors.toList());
    }

    private Inci addFunction(Inci inci, Function function) {
        List<Function> functions = inci.getFunctions();
        functions.add(function);
        inci.setFunctions(functions);
        return inci;
    }
}
