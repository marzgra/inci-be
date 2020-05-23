package com.inci.repository;

import com.inci.entity.PolishName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PolishNameRepository extends JpaRepository<PolishName, Long> {

}