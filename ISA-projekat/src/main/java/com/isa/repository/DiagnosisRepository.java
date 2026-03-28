package com.isa.repository;

import com.isa.domain.model.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {

    List<Diagnosis> findAllByDepartmentId(Long departmentId);

    Optional<Diagnosis> findByCodeAndDepartmentId(String code, Long departmentId);
}
