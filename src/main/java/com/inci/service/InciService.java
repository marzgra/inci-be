package com.inci.service;

import com.inci.dto.*;
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

import static com.inci.enums.Type.*;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class InciService {
    private final String EMOLIENT = "emolient";
    private final String PROTEINS = "proteiny";
    private final String HUMECTANTS = "humectant";
    private final String DETERGENT = "silny detergent";

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
        if (!name.isBlank() || !name.isEmpty()) {
            return alternativeNameRepository.findByAlternativeName(name)
                    .map(AlternativeName::getInci)
                    .map(inci -> mapper.map(inci, InciDto.class))
                    .orElse(new InciDto("not found: " + name));
        }
        return new InciDto("not found: " + name);
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
                .collect(toList()));

        return pageDto;
    }

    public List<SearchDto> search(String part) {
        log.info("Looking for INCI with any name like: " + part);
        List<SearchDto> result = alternativeNameRepository.findAllByAlternativeNameContaining(part.toLowerCase())
                .stream()
                .map(inci -> mapper.map(inci, SearchDto.class)).collect(toList());

        result.addAll(polishNameRepository.findAllByPolishNameContaining(part.toLowerCase())
                .stream()
                .map(inci -> mapper.map(inci, SearchDto.class))
                .collect(toList()));

        result.addAll(inciRepository.findAllByInciNameContaining(part.toLowerCase())
                .stream()
                .map(inci -> mapper.map(inci, SearchDto.class))
                .collect(toList()));

        result.forEach(dto -> dto.setSimilarity(levenshteinDistanceService.calculateSimilarity(part.toLowerCase(), dto.getName())));

        return result
                .stream()
                .filter(dto -> dto.getSimilarity() >= 0)
                .sorted(comparing(SearchDto::getSimilarity).reversed())
                .collect(toList());
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
                .collect(toList());
    }

    private Inci addFunction(Inci inci, Function function) {
        List<Function> functions = inci.getFunctions();
        functions.add(function);
        inci.setFunctions(functions);
        return inci;
    }

    public AnalyzeResultDto analyze(List<String> list) {
        List<InciDto> ingredients = list.stream()
                .map(String::trim)
                .map(String::toLowerCase)
                .map(this::findByName)
                .collect(toList());
        AnalyzeResultDto analyzeResultDto = new AnalyzeResultDto();
        analyzeResultDto.setIngredients(ingredients);
        analyzeResultDto.setType(getPehType(ingredients));
        return analyzeResultDto;
    }

    private String getPehType(List<InciDto> ingredients) {
        long proteins = 0;
        long humectants = 0;
        long emolients = 0;

        for (int i = 0; i < ingredients.size(); i++) {
            long weight = ingredients.size() - i;
            InciDto inci = ingredients.get(i);
            if (countGivenFunction(inci, DETERGENT) > 0) {
                return HARSH_DETERGENT.getValue();
            }
            proteins += countGivenFunction(inci, PROTEINS) * weight;
            humectants += countGivenFunction(inci, HUMECTANTS) * weight;
            emolients += countGivenFunction(inci, EMOLIENT) * weight;
        }
        return getPehTypeBasedOnWeightedIngredients(proteins, emolients, humectants);
    }

    private long countGivenFunction(InciDto inci, String function) {
        return inci.getFunctionNames()
                .stream()
                .map(FunctionDto::getFunctionName)
                .filter(name -> name.equals(function))
                .count();
    }

    private String getPehTypeBasedOnWeightedIngredients(long proteins, long emolients, long humectants) {
        if (proteins > 0 && emolients > 0 && humectants > 0) {
            return PEH.getValue();
        }
        if (proteins == 0 && emolients > 0 && humectants > 0 && humectants > emolients) {
            return HE.getValue();
        }
        if (proteins == 0 && humectants > 0 && humectants < emolients) {
            return EH.getValue();
        }
        if (emolients > 0 && humectants == 0 && proteins > emolients) {
            return PE.getValue();
        }
        if (emolients > 0 && humectants == 0 && proteins < emolients) {
            return EP.getValue();
        }
        if (emolients == 0 && humectants > 0 && proteins < humectants) {
            return PH.getValue();
        }
        if (emolients == 0 && humectants > 0 && proteins > humectants) {
            return HP.getValue();
        }
        return null;
    }

}
