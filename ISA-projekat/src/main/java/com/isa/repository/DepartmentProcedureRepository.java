package com.isa.repository;

import com.isa.domain.model.DepartmentProcedure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentProcedureRepository extends JpaRepository<DepartmentProcedure, Long> {
}
