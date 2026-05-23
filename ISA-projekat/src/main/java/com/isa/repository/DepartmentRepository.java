package com.isa.repository;

import com.isa.domain.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    List<Department> findAllByHospitalId(Long hospitalId);

    long countByName(String name);

    @Query("""
            SELECT department FROM Department department
            WHERE (:hospitalId IS NULL OR department.hospital.id = :hospitalId)
              AND (:name IS NULL OR :name = '' OR LOWER(department.name) LIKE LOWER(CONCAT('%', :name, '%')))
            """)
    List<Department> findAllByHospitalIdAndName(@Param("hospitalId") Long hospitalId, @Param("name") String name);

}
