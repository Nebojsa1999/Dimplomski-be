package com.isa.service;

import com.isa.domain.dto.AppointmentDTO;
import com.isa.domain.model.Appointment;
import com.isa.domain.model.Hospital;
import com.isa.enums.AppointmentStatus;
import com.isa.exception.NotFoundException;
import com.isa.repository.AppointmentRepository;
import com.isa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Appointment create(AppointmentDTO appointmentDTO) {
        final Appointment appointment = new Appointment();
        appointment.setDuration(Integer.parseInt(appointmentDTO.getDuration()));
        appointment.setDoctor(userRepository.findById(appointmentDTO.getDoctorId()).orElseThrow(NotFoundException::new));
        appointment.setDateAndTime(Instant.parse(appointmentDTO.getDateAndTime()));
        appointment.setHospital(appointment.getDoctor().getHospital());
        appointmentRepository.save(appointment);
        return appointment;
    }

    public List<Appointment> getFreeAppointments(Hospital hospital) {
        return appointmentRepository.findAllByHospitalId(hospital.getId()).stream()
                .filter(appointment -> appointment.getPatient() == null  && appointment.getDateAndTime().isAfter(Instant.now()))
                .toList();
    }

    public List<Appointment> getAppointmentsForHospital(Hospital hospital, AppointmentStatus appointmentStatus) {
        return appointmentRepository.findAllByHospitalId(hospital.getId()).stream()
                .filter(appointment -> appointment.getPatient() != null && appointmentStatus == appointment.getAppointmentStatus())
                .toList();
    }

    public void removeUserFromAppointment(Appointment appointment) {
        appointment.setPatient(null);
        appointmentRepository.save(appointment);
    }

    public Optional<Appointment> get(long id) {
        return appointmentRepository.findById(id);
    }

    public void save(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    public List<Appointment> getScheduledAndNotFinishedAppointmentsBasedOnDate(Hospital hospital, String date) {
        final DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-M-d");
        final LocalDate ldt = LocalDate.parse(date, f);

        return appointmentRepository.findAllByHospitalId(hospital.getId()).stream()
                .filter(appointment -> appointment.getPatient() != null && appointment.getAppointmentStatus() == AppointmentStatus.SCHEDULED
                && appointment.getDateAndTime().atOffset(ZoneOffset.UTC).toLocalDate().equals(ldt))
                .toList();
    }

}
