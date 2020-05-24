package com.inci.mapper;

import com.inci.dto.InciDto;
import com.inci.entity.Inci;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

@Component
public class InciMapper extends ConfigurableMapper {

    protected void configure(MapperFactory factory) {
        factory.classMap(Inci.class, InciDto.class)
                .field("functions{functionName}", "functionNames{}")
                .field("polishNames{polishName}", "polishNames{}")
                .field("alternativeNames{alternativeName}", "alternativeNames{}")
                .byDefault()
                .register();
    }

//    @Override
//    public void registerConfiguration(ClassMapBuilder<Inci, InciDto> builder) {
//        builder.byDefault()
//                .field("functions{functionName}", "functionNames{}")
//                .field("polishNames{polishName}", "polishNames{}")
//                .field("alternativeNames{alternativeName}", "alternativeNames{}")
//                .byDefault()
//                .register();
//    }
//
//    @Override
//    public void mapBtoA(InciDto inciDto, Inci inci, MappingContext context) {
//
//    }


}
