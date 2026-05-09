package com.isa.repository;

import com.isa.domain.model.Medicament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicamentRepository extends JpaRepository<Medicament, Long> {

    List<Medicament> findAllByDepartmentNameName(String departmentName);

    @Query("""
            SELECT medicament FROM Medicament medicament
            WHERE (:departmentName IS NULL OR medicament.departmentName.name = :departmentName)
              AND (:name IS NULL OR :name = '' OR LOWER(medicament.name) LIKE LOWER(CONCAT('%', :name, '%')))
            """)
    List<Medicament> findAllFiltered(@Param("departmentName") String departmentName, @Param("name") String name);
}
