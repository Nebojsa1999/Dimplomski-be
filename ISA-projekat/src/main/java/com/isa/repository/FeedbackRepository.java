package com.isa.repository;

import com.isa.domain.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    Optional<Feedback> findByAppointmentId(long appointmentId);

    boolean existsByAppointmentId(long appointmentId);

    @Query("SELECT AVG(f.grade) FROM Feedback f WHERE f.appointment.doctor.id = :doctorId")
    Optional<Double> findAverageGradeByDoctorId(@Param("doctorId") Long doctorId);

    @Query("SELECT AVG(f.grade) FROM Feedback f WHERE f.appointment.doctor.hospital.id = :hospitalId")
    Optional<Double> findAverageGradeByHospitalId(@Param("hospitalId") Long hospitalId);
}
