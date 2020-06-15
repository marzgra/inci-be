package com.inci.service;

import com.inci.dto.FunctionDto;
import com.inci.entity.Function;
import com.inci.mapper.Mapper;
import com.inci.repository.FunctionRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class FunctionService {
    private final Logger log = LoggerFactory.getLogger(FunctionService.class);

    private final FunctionRepository functionRepository;
    private final Mapper mapper;

    @Transactional
    public Long addNewFunction(FunctionDto function) {
        log.info("Creating new function: " + function.getFunctionName());
        return functionRepository.findByFunctionName(function.getFunctionName())
                .map(Function::getFunctionId)
                .orElse(createNewFunction(function.getFunctionName()));
    }

    private Long createNewFunction(String functionName) {
        return functionRepository.save(new Function(functionName)).getFunctionId();
    }


    public List<FunctionDto> getAllFunctions() {
        log.info("Getting all functions: ");
        return functionRepository.findAllByOrderByFunctionName()
                .stream()
                .map(function -> mapper.map(function, FunctionDto.class))
                .collect(toList());
    }
}
