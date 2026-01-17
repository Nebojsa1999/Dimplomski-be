package com.isa.repository;

import com.isa.domain.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    @Query("""
            select equipment
                        FROM Equipment equipment
                                    WHERE :searchFilter IS NULL OR :searchFilter = '' OR  LOWER(equipment.name) LIKE LOWER(CONCAT('%', :searchFilter, '%'))
            """)
    List<Equipment> findAllByName(@Param("searchFilter") String searchFilter);

    @Query("""
            SELECT equipment
                   FROM Equipment equipment
                     WHERE equipment.hospital.id = :hospitalId AND ( :searchFilter IS NULL OR :searchFilter = '' OR  LOWER(equipment.name) LIKE LOWER(CONCAT('%', :searchFilter, '%')))
            """)
    List<Equipment> findAllByHospitalId(Long hospitalId, @Param("searchFilter") String searchFilter);
}
