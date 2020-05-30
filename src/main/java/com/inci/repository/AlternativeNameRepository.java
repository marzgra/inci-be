package com.inci.repository;

import com.inci.entity.AlternativeName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlternativeNameRepository extends JpaRepository<AlternativeName, Long> {
    List<AlternativeName> findAllByAlternativeNameContaining(String part);
}