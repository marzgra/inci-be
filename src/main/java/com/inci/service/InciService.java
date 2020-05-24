package com.inci.service;

import com.inci.dto.InciDto;
import com.inci.entity.Inci;
import com.inci.mapper.InciMapper;
import com.inci.repository.InciRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
        return inciRepository.saveAndFlush(newInci).getInciId();
    }
}
