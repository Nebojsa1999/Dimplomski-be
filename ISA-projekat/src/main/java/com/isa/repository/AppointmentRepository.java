package com.isa.repository;

import com.isa.domain.model.Appointment;
import com.isa.domain.model.User;
import com.isa.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findAllByPatientId(Long id);

    List<Appointment> findAllByHospitalId(Long id);

    @Query("SELECT appointment FROM Appointment appointment WHERE (:appointmentStatus is NULL OR appointment.appointmentStatus = :appointmentStatus) AND appointment.hospital.id =:hospital" +
            " AND (:from IS NULL OR appointment.dateAndTime >= :from)\n" +
            "      AND (:to IS NULL OR appointment.dateAndTime <= :to)\n")
    List<Appointment> findAllByHospitalId(@Param("appointmentStatus") AppointmentStatus appointmentStatus, @Param("from") Instant from, @Param("to") Instant to, @Param("hospital") Long hospital);

    @Query("SELECT appointment FROM Appointment appointment WHERE (:appointmentStatus is NULL OR appointment.appointmentStatus = :appointmentStatus)" +
            " AND (:from IS NULL OR appointment.dateAndTime >= :from)\n" +
            "      AND (:to IS NULL OR appointment.dateAndTime <= :to)\n")
    List<Appointment> findAll(@Param("appointmentStatus") AppointmentStatus appointmentStatus, @Param("from") Instant from, @Param("to") Instant to);

    @Query("SELECT appointment FROM Appointment appointment WHERE appointment.dateAndTime < :timestamp and appointment.appointmentStatus = :status")
    List<Appointment> findAllOpenedInThePast(@Param("timestamp") Instant timestamp, @Param("status") AppointmentStatus status);

    @Query("SELECT appointment FROM Appointment appointment WHERE appointment.dateAndTime >= :start and appointment.dateAndTime < :end and appointment.doctor = :doctor")
    List<Appointment> findAllByDurationAndDateTimeAndDoctor(@Param("start") Instant start, @Param("end") Instant end, @Param("doctor") User Doctor);
}
