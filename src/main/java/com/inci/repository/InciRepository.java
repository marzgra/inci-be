package com.inci.repository;

import com.inci.entity.Inci;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InciRepository extends PagingAndSortingRepository<Inci, Long> {
    Optional<Inci> findByInciName(String name);

    List<Inci> findAllByInciNameContaining(String part);

    @Query(value = "select count(*) from inci", nativeQuery = true)
    Long numberOfEntries();
}