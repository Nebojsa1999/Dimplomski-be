package com.isa.repository;

import com.isa.domain.model.AppointmentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppointmentReportRepository extends JpaRepository<AppointmentReport, Long> {

    Optional<AppointmentReport> findAppointmentReportsByAppointmentId(Long appointmentId);
}
