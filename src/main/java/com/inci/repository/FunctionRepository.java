package com.inci.repository;

import com.inci.entity.Function;
import com.inci.entity.Inci;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface FunctionRepository extends JpaRepository<Function, Long> {
    Optional<Function> findByFunctionName(String functionName);

    List<Function> findAllByOrderByFunctionName();

}