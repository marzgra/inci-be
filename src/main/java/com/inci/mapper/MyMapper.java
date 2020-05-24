package com.inci.mapper;

import com.inci.dto.InciDto;
import com.inci.entity.Inci;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.metadata.ClassMapBuilder;

public abstract class MyMapper<A, B> extends CustomMapper<A, B> {

    public void registerConfiguration(ClassMapBuilder<A, B> builder) {
        builder.byDefault()
                .byDefault()
                .register();
    }
}
