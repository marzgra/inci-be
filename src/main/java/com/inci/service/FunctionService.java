package com.inci.service;

import com.inci.dto.FunctionDto;
import com.inci.entity.Function;
import com.inci.mapper.Mapper;
import com.inci.repository.FunctionRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class FunctionService {

    private final FunctionRepository functionRepository;
    private final Mapper mapper;

    public FunctionService(final FunctionRepository functionRepository,
                           final Mapper mapper) {
        this.functionRepository = functionRepository;
        this.mapper = mapper;
    }

    @Transactional
    public Long addNewFunction(String functionName) {
        return functionRepository.findByFunctionName(functionName)
                .map(Function::getFunctionId)
                .orElse(createNewFunction(functionName));
    }

    private Long createNewFunction(String functionName) {
        return functionRepository.save(new Function(functionName)).getFunctionId();
    }


    public List<FunctionDto> getAllFunctions() {
        return functionRepository.findAll()
                .stream()
                .map(function -> mapper.map(function, FunctionDto.class))
                .collect(toList());
    }
}
