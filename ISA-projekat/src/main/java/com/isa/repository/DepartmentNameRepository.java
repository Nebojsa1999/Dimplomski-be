package com.isa.repository;

import com.isa.domain.model.DepartmentName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentNameRepository extends JpaRepository<DepartmentName, Long> {
    Optional<DepartmentName> findByName(String name);
}
