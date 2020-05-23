package com.inci.repository;

import com.inci.entity.Inci;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InciRepository extends JpaRepository<Inci, Long> {
    Optional<Inci> findByInciName(String name);
}