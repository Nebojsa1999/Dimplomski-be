package com.isa.repository;

import com.isa.domain.model.DepartmentProcedure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentProcedureRepository extends JpaRepository<DepartmentProcedure, Long> {

    @Query("""
            SELECT p FROM DepartmentProcedure p
            WHERE (:departmentId IS NULL OR p.department.id = :departmentId)
              AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
            """)
    List<DepartmentProcedure> findAllFiltered(@Param("departmentId") Long departmentId,
                                              @Param("name") String name);
}
