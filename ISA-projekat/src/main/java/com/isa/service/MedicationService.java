package com.isa.service;

import com.isa.domain.model.Medication;
import com.isa.repository.MedicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MedicationService {

    private final MedicationRepository medicationRepository;

    @Autowired
    public MedicationService(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    public Medication create(Medication medication) {
        return medicationRepository.save(medication);
    }

    public Optional<Medication> get(Long id) {
        return medicationRepository.findById(id);
    }

    public void update(Medication medication, Medication medicationDto) {
        medication.setName(medicationDto.getName());
        medication.setDosage(medicationDto.getDosage());
        medication.setFrequency(medicationDto.getFrequency());
        medication.setPrescription(medicationDto.getPrescription());
        medicationRepository.save(medication);
    }
}
