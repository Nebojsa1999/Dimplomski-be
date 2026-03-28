package com.isa.service;

import com.isa.domain.dto.PatientMedicalRecordDTO;
import com.isa.domain.model.PatientMedicalRecord;
import com.isa.domain.model.User;
import com.isa.repository.PatientMedicalRecordRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientMedicalRecordService {

    private final PatientMedicalRecordRepository patientMedicalRecordRepository;

    @Autowired
    public PatientMedicalRecordService(PatientMedicalRecordRepository patientMedicalRecordRepository) {
        this.patientMedicalRecordRepository = patientMedicalRecordRepository;
    }

    @Transactional
    public PatientMedicalRecord create(PatientMedicalRecordDTO dto, User patient) {
        final PatientMedicalRecord record = new PatientMedicalRecord();
        mapDtoToEntity(dto, record);
        record.setPatient(patient);
        return patientMedicalRecordRepository.save(record);
    }

    public Optional<PatientMedicalRecord> get(Long id) {
        return patientMedicalRecordRepository.findById(id);
    }

    public Optional<PatientMedicalRecord> getByPatient(Long patientId) {
        return patientMedicalRecordRepository.findByPatientId(patientId);
    }

    @Transactional
    public PatientMedicalRecord update(PatientMedicalRecord record, PatientMedicalRecordDTO dto) {
        mapDtoToEntity(dto, record);
        return patientMedicalRecordRepository.save(record);
    }

    public void delete(PatientMedicalRecord record) {
        patientMedicalRecordRepository.delete(record);
    }

    private void mapDtoToEntity(PatientMedicalRecordDTO dto, PatientMedicalRecord record) {
        record.setBloodType(dto.getBloodType());
        record.setRhFactor(dto.getRhFactor());
        record.setHeightCm(dto.getHeightCm());
        record.setWeightKg(dto.getWeightKg());
        record.setChronicDiseases(dto.getChronicDiseases());
        record.setPreviousHospitalization(dto.getPreviousHospitalization());
        record.setPreviousSurgeries(dto.getPreviousSurgeries());
        record.setFamilyHistory(dto.getFamilyHistory());
        record.setAllergies(dto.getAllergies());
        record.setLongThermTherapy(dto.getLongThermTherapy());
        record.setSpecificContradictions(dto.getSpecificContradictions());
    }
}
