package com.isa.repository;

import com.isa.domain.model.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {

    List<Diagnosis> findAllByDepartmentNameName(String departmentName);

    List<Diagnosis> findAllByNameContainingIgnoreCase(String name);

    List<Diagnosis> findAllByNameContainingIgnoreCaseAndDepartmentNameName(String name, String departmentName);

    Optional<Diagnosis> findByCodeAndDepartmentNameName(String code, String departmentName);
}
