package com.isa.service;

import com.isa.domain.model.Medication;
import com.isa.domain.model.Prescription;
import com.isa.repository.PrescriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final MedicationService medicationService;

    @Autowired
    public PrescriptionService(PrescriptionRepository prescriptionRepository, MedicationService medicationService) {
        this.prescriptionRepository = prescriptionRepository;
        this.medicationService = medicationService;
    }

    public Prescription create(Medication medication, Prescription prescription) {
        final Prescription createdPrescription = prescriptionRepository.save(prescription);
        medication.setPrescription(createdPrescription);
        medicationService.create(medication);

        return createdPrescription;
    }

    public Optional<Prescription> get(Long id) {
        return prescriptionRepository.findById(id);
    }

    public void update(Prescription prescriptionDto, Prescription prescription) {
        prescription.setDoctor(prescriptionDto.getDoctor());
        prescription.setPatient(prescriptionDto.getPatient());
        prescription.setNotes(prescriptionDto.getNotes());
        prescription.setIssuedAt(prescriptionDto.getIssuedAt());
        prescriptionRepository.save(prescription);
    }
}
