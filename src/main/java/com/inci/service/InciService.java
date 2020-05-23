package com.inci.service;

import com.inci.dto.InciDto;
import com.inci.entity.Inci;
import com.inci.mapper.InciMapper;
import com.inci.repository.InciRepository;
import org.springframework.stereotype.Service;

@Service
public class InciService {

    private final InciRepository inciRepository;
    private final InciMapper mapper;


    public InciService(final InciRepository inciRepository,
                       final InciMapper mapper) {
        this.inciRepository = inciRepository;
        this.mapper = mapper;
    }

    public InciDto findByName(String name) {
        return inciRepository.findByInciName(name)
                .map(inci -> mapper.map(inci, InciDto.class))
                .orElseGet(null);
    }

    public InciDto getOne() {
        Inci one = inciRepository.findById(1275L).orElse(null);

        if (one != null) {
            return mapper.map(one, InciDto.class);
        }

        return null;
    }

}
