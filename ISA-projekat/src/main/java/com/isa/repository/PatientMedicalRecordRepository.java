package com.isa.repository;

import com.isa.domain.model.PatientMedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientMedicalRecordRepository extends JpaRepository<PatientMedicalRecord, Long> {

    Optional<PatientMedicalRecord> findByPatientId(Long patientId);
}
