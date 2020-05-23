package com.inci.repository;

import com.inci.entity.AlternativeName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AlternativeNameRepository extends JpaRepository<AlternativeName, Long> {

}