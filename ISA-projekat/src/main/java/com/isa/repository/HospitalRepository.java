package com.isa.repository;

import com.isa.domain.model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {

    @Query("""
              SELECT h
              FROM Hospital h
              WHERE LOWER(h.name) LIKE LOWER(CONCAT('%', :searchFilter, '%'))
            """)
    List<Hospital> findByName(@Param("searchFilter") String searchFilter);
}
