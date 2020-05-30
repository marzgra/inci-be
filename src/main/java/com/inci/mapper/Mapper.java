package com.inci.mapper;

import com.inci.dto.FunctionDto;
import com.inci.dto.InciDto;
import com.inci.dto.SearchDto;
import com.inci.entity.AlternativeName;
import com.inci.entity.Function;
import com.inci.entity.Inci;
import com.inci.entity.PolishName;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

@Component
public class Mapper extends ConfigurableMapper {

    protected void configure(MapperFactory factory) {
        factory.classMap(Inci.class, InciDto.class)
                .field("functions{}", "functionNames{}")
                .field("polishNames{polishName}", "polishNames{}")
                .field("alternativeNames{alternativeName}", "alternativeNames{}")
                .byDefault()
                .register();

        factory.classMap(Function.class, FunctionDto.class)
                .byDefault()
                .register();

        factory.classMap(AlternativeName.class, SearchDto.class)
                .field("alternativeName", "name")
                .field("inci.inciId", "inciId")
                .byDefault()
                .register();

        factory.classMap(PolishName.class, SearchDto.class)
                .field("polishName", "name")
                .field("inci.inciId", "inciId")
                .byDefault()
                .register();

        factory.classMap(Inci.class, SearchDto.class)
                .field("inciName", "name")
                .byDefault()
                .register();
    }

}
