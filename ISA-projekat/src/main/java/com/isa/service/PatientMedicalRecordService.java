package com.isa.service;

import com.isa.domain.model.PatientMedicalRecord;
import com.isa.repository.PatientMedicalRecordRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientMedicalRecordService {

    private final PatientMedicalRecordRepository patientMedicalRecordRepository;

    @Autowired
    public PatientMedicalRecordService(PatientMedicalRecordRepository patientMedicalRecordRepository) {
        this.patientMedicalRecordRepository = patientMedicalRecordRepository;
    }

    @Transactional
    public PatientMedicalRecord create(PatientMedicalRecord dto) {
        final PatientMedicalRecord patientMedicalRecord = new PatientMedicalRecord();
        patientMedicalRecord.setPatient(dto.getPatient());
        patientMedicalRecord.setAllergies(dto.getAllergies());
        patientMedicalRecord.setBloodType(dto.getBloodType());
        patientMedicalRecord.setHeightCm(dto.getHeightCm());
        patientMedicalRecord.setWeightKg(dto.getWeightKg());
        patientMedicalRecord.setChronicDiseases(dto.getChronicDiseases());
        patientMedicalRecord.setFamilyHistory(dto.getFamilyHistory());
        patientMedicalRecord.setLongThermTherapy(dto.getLongThermTherapy());
        patientMedicalRecord.setSpecificContradictions(dto.getSpecificContradictions());
        patientMedicalRecord.setRhFactor(dto.getRhFactor());
        patientMedicalRecord.setPreviousSurgeries(dto.getPreviousSurgeries());
        patientMedicalRecord.setPreviousHospitalization(dto.getPreviousHospitalization());

        return patientMedicalRecordRepository.save(patientMedicalRecord);
    }
}
