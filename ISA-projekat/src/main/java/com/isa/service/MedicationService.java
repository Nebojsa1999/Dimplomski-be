package com.isa.service;

import com.isa.domain.model.Appointment;
import com.isa.domain.model.Medication;
import com.isa.enums.AppointmentStatus;
import com.isa.repository.AppointmentRepository;
import com.isa.repository.MedicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class MedicationService {

    private final MedicationRepository medicationRepository;
    private final AppointmentRepository appointmentRepository;

    @Autowired
    public MedicationService(MedicationRepository medicationRepository, AppointmentRepository appointmentRepository) {
        this.medicationRepository = medicationRepository;
        this.appointmentRepository = appointmentRepository;
    }

    public Medication create(Medication medication, Appointment appointment) {
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
        medication.setName(medicationDto.getName());
        medication.setDosage(medicationDto.getDosage());
        medication.setFrequency(medicationDto.getFrequency());
        medicationRepository.save(medication);
    }
}
