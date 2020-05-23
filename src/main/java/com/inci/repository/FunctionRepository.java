package com.inci.repository;

import com.inci.entity.Function;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FunctionRepository extends JpaRepository<Function, Long> {

}