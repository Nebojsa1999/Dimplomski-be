package com.isa.service;

import com.isa.domain.dto.MedicationDto;
import com.isa.domain.model.Appointment;
import com.isa.domain.model.Medication;
import com.isa.enums.AppointmentStatus;
import com.isa.exception.NotFoundException;
import com.isa.repository.AppointmentRepository;
import com.isa.repository.MedicamentRepository;
import com.isa.repository.MedicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class MedicationService {

    private final MedicationRepository medicationRepository;
    private final AppointmentRepository appointmentRepository;
    private final MedicamentRepository medicamentRepository;

    @Autowired
    public MedicationService(MedicationRepository medicationRepository, AppointmentRepository appointmentRepository, MedicamentRepository medicamentRepository) {
        this.medicationRepository = medicationRepository;
        this.appointmentRepository = appointmentRepository;
        this.medicamentRepository = medicamentRepository;
    }

    public Medication create(MedicationDto dto, Appointment appointment) {
        final Medication medication = new Medication();
        medication.setFrequency(dto.getFrequency());
        medication.setNotes(dto.getNotes());
        final com.isa.domain.model.Medicament medicament = medicamentRepository.findById(dto.getMedicamentId()).orElseThrow(() -> new NotFoundException("Medicament not found"));
        if (dto.getName() != null) medicament.setName(dto.getName());
        if (dto.getDosage() != null) medicament.setDosage(dto.getDosage());
        if (dto.getInstructions() != null) medicament.setInstructions(dto.getInstructions());
        medication.setMedicament(medicament);
        medication.setAppointment(appointment);
        medication.setIssuedAt(Instant.now());
        appointment.setAppointmentStatus(AppointmentStatus.FINISHED);
        appointmentRepository.save(appointment);
        return medicationRepository.save(medication);
    }

    public Optional<Medication> get(Long id) {
        return medicationRepository.findById(id);
    }

    public Optional<Medication> findByAppointment(Appointment appointment) {
        return medicationRepository.findByAppointmentId(appointment.getId());
    }

    public void update(Medication medication, Medication medicationDto) {
        medication.getMedicament().setName(medicationDto.getMedicament().getName());
        medication.getMedicament().setDosage(medicationDto.getMedicament().getDosage());
        medication.setFrequency(medicationDto.getFrequency());
        medicationRepository.save(medication);
    }
}
