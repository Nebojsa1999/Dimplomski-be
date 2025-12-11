package com.isa.service;

import com.isa.domain.dto.AppointmentDTO;
import com.isa.domain.model.Appointment;
import com.isa.domain.model.CenterAccount;
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
        appointment.setAdmin(userRepository.findById(appointmentDTO.getAdminOfCenterId()).orElseThrow(NotFoundException::new));
        appointment.setDateAndTime(Instant.parse(appointmentDTO.getDateAndTime()));
        appointment.setCenterAccount(appointment.getAdmin().getCenterAccount());
        appointmentRepository.save(appointment);
        return appointment;
    }

    public List<Appointment> getFreeAppointments(CenterAccount centerAccount) {
        return appointmentRepository.findAllByCenterAccountId(centerAccount.getId()).stream()
                .filter(appointment -> appointment.getPatient() == null  && appointment.getDateAndTime().isAfter(Instant.now()))
                .toList();
    }

    public List<Appointment> getAppointmentsForCenterAccount(CenterAccount centerAccount, boolean isCompleted) {
        return appointmentRepository.findAllByCenterAccountId(centerAccount.getId()).stream()
                .filter(appointment -> appointment.getPatient() != null && (isCompleted != appointment.isCompletedAppointment()))
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

    public List<Appointment> getScheduledAndNotFinishedAppointmentsBasedOnDate(CenterAccount centerAccount, String date) {
        final DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-M-d");
        final LocalDate ldt = LocalDate.parse(date, f);

        return appointmentRepository.findAllByCenterAccountId(centerAccount.getId()).stream()
                .filter(appointment -> appointment.getPatient() != null && !appointment.isCompletedAppointment()
                && appointment.getDateAndTime().atOffset(ZoneOffset.UTC).toLocalDate().equals(ldt))
                .toList();
    }

}
