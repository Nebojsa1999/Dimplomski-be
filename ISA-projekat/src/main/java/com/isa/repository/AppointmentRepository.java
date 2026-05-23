package com.isa.repository;

import com.isa.domain.model.Appointment;
import com.isa.enums.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findAllByDoctorHospitalId(Long id);

    @Query("""
            SELECT appointment FROM Appointment appointment WHERE (:appointmentStatus is NULL OR appointment.appointmentStatus = :appointmentStatus) AND appointment.doctor.hospital.id =:hospital
             AND (:from IS NULL OR appointment.dateAndTime >= :from)
                  AND (:to IS NULL OR appointment.dateAndTime <= :to)
            """)
    List<Appointment> findAllByHospitalId(@Param("appointmentStatus") AppointmentStatus appointmentStatus, @Param("from") Instant from, @Param("to") Instant to, @Param("hospital") Long hospital);

    @Query("""
            SELECT appointment FROM Appointment appointment WHERE (:appointmentStatus is NULL OR appointment.appointmentStatus = :appointmentStatus)
             AND (:from IS NULL OR appointment.dateAndTime >= :from)
                  AND (:to IS NULL OR appointment.dateAndTime <= :to)
            """)
    List<Appointment> findAll(@Param("appointmentStatus") AppointmentStatus appointmentStatus, @Param("from") Instant from, @Param("to") Instant to);

    @Query("""
            SELECT appointment FROM Appointment appointment
            WHERE appointment.doctor.id = :doctorId
              AND (appointment.appointmentStatus = 'SCHEDULED' OR appointment.appointmentStatus = 'FINISHED')
              AND appointment.dateAndTime >= :start
              AND appointment.dateAndTime < :end
            """)
    List<Appointment> findConflictingForDoctor(@Param("doctorId") Long doctorId, @Param("start") Instant start, @Param("end") Instant end);

    @Query("""
            SELECT appointment FROM Appointment appointment
            WHERE appointment.patient.id = :patientId
              AND (:status IS NULL OR appointment.appointmentStatus = :status)
              AND (:from IS NULL OR appointment.dateAndTime >= :from)
              AND (:to IS NULL OR appointment.dateAndTime <= :to)
            ORDER BY
              CASE WHEN appointment.appointmentStatus = 'SCHEDULED' THEN 0 ELSE 1 END ASC,
              CASE WHEN appointment.appointmentStatus = 'SCHEDULED' THEN appointment.dateAndTime END ASC,
              CASE WHEN appointment.appointmentStatus != 'SCHEDULED' THEN appointment.dateAndTime END DESC
            """)
    Page<Appointment> findByPatient(@Param("patientId") Long patientId, @Param("status") AppointmentStatus status, @Param("from") Instant from, @Param("to") Instant to, Pageable pageable);

    @Query("""
            SELECT appointment FROM Appointment appointment
            WHERE appointment.doctor.id = :doctorId
              AND (:status IS NULL OR appointment.appointmentStatus = :status)
              AND (:from IS NULL OR appointment.dateAndTime >= :from)
              AND (:to IS NULL OR appointment.dateAndTime <= :to)
            ORDER BY
              CASE WHEN appointment.appointmentStatus = 'SCHEDULED' THEN 0 ELSE 1 END ASC,
              CASE WHEN appointment.appointmentStatus = 'SCHEDULED' THEN appointment.dateAndTime END ASC,
              CASE WHEN appointment.appointmentStatus != 'SCHEDULED' THEN appointment.dateAndTime END DESC
            """)
    Page<Appointment> findByDoctor(@Param("doctorId") Long doctorId, @Param("status") AppointmentStatus status, @Param("from") Instant from, @Param("to") Instant to, Pageable pageable);

    @Query("""
            SELECT appointment FROM Appointment appointment
            WHERE appointment.doctor.id = :doctorId
              AND appointment.dateAndTime >= :monthStart
              AND appointment.dateAndTime < :monthEnd
            ORDER BY appointment.dateAndTime ASC
            """)
    List<Appointment> findByDoctorAndMonth(@Param("doctorId") Long doctorId, @Param("monthStart") Instant monthStart, @Param("monthEnd") Instant monthEnd);

}
