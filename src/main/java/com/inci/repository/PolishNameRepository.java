package com.inci.repository;

import com.inci.entity.PolishName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PolishNameRepository extends JpaRepository<PolishName, Long> {
    List<PolishName> findAllByPolishNameContaining(String part);
}