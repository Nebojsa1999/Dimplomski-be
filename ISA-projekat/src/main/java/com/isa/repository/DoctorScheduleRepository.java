package com.isa.repository;

import com.isa.domain.model.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {

    List<DoctorSchedule> findAllByDoctorId(Long doctorId);

    @Query("""
            SELECT s FROM DoctorSchedule s
            WHERE (:doctorId IS NULL OR s.doctor.id = :doctorId)
              AND s.startDate <= :endDate
              AND s.endDate >= :startDate
            """)
    List<DoctorSchedule> findAllInDateRange(@Param("doctorId") Long doctorId,
                                            @Param("startDate") Instant startDate,
                                            @Param("endDate") Instant endDate);

    @Query("""
            SELECT s FROM DoctorSchedule s
            WHERE s.doctor.id = :doctorId
              AND s.dayOfWeek = :dayOfWeek
              AND s.id <> :excludeId
              AND s.startTime < :endTime
              AND s.endTime > :startTime
            """)
    List<DoctorSchedule> findOverlapping(@Param("doctorId") Long doctorId,
                                         @Param("dayOfWeek") DayOfWeek dayOfWeek,
                                         @Param("startTime") LocalTime startTime,
                                         @Param("endTime") LocalTime endTime,
                                         @Param("excludeId") Long excludeId);
}
