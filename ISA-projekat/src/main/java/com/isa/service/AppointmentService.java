package com.isa.service;

import com.isa.domain.dto.AppointmentDTO;
import com.isa.domain.model.Appointment;
import com.isa.domain.model.Hospital;
import com.isa.domain.model.User;
import com.isa.enums.AppointmentStatus;
import com.isa.enums.DoctorType;
import com.isa.enums.Role;
import com.isa.exception.NotFoundException;
import com.isa.repository.AppointmentRepository;
import com.isa.repository.HospitalRepository;
import com.isa.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final HospitalRepository hospitalRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, UserRepository userRepository, HospitalRepository hospitalRepository) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.hospitalRepository = hospitalRepository;
    }

    @Transactional
    public Appointment create(AppointmentDTO appointmentDTO) {
        final Appointment appointment = new Appointment();
        appointment.setDuration(Integer.parseInt(appointmentDTO.getDuration()));
        appointment.setDoctor(userRepository.findById(appointmentDTO.getDoctorId()).orElseThrow(NotFoundException::new));
        final Instant instant = Instant.parse(appointmentDTO.getDateAndTime());
        final ZonedDateTime zdt = instant.atZone(ZoneId.of("Europe/Belgrade"));
        appointment.setDateAndTime(zdt.toInstant());
        appointment.setHospital(appointment.getDoctor().getHospital());
        appointment.setAppointmentStatus(AppointmentStatus.OPEN);
        appointmentRepository.save(appointment);
        return appointment;
    }

    public List<Appointment> list(AppointmentStatus appointmentStatus, Instant from, Instant to) {
        return appointmentRepository.findAll(appointmentStatus, from, to);
    }

    public List<Appointment> listByHospital(AppointmentStatus appointmentStatus, Instant from, Instant to, Hospital hospital, DoctorType doctorType) {
        return appointmentRepository.findAllByHospitalId(appointmentStatus, from, to, hospital.getId(),doctorType);
    }

    public Appointment schedule(Appointment appointment, User patient) {
        if (appointment.getDateAndTime().toEpochMilli() < Instant.now().toEpochMilli()) {
            throw new IllegalArgumentException("Can't schedule an appointment in the past.");
        }

        appointment.setPatient(patient);
        appointment.setAppointmentStatus(AppointmentStatus.SCHEDULED);
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getFreeAppointments(Hospital hospital) {
        return appointmentRepository.findAllByHospitalId(hospital.getId()).stream()
                .filter(appointment -> appointment.getPatient() == null && appointment.getDateAndTime().isAfter(Instant.now()))
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

    @Transactional
    public void deleteFromPast(Instant now, AppointmentStatus appointmentStatus) {
        final List<Appointment> allOpenedInThePast = appointmentRepository.findAllOpenedInThePast(now, appointmentStatus);
        appointmentRepository.deleteAll(allOpenedInThePast);
    }

    public void addAppointments(Instant dateTime) {
        hospitalRepository.findAll().forEach(hospital -> userRepository.findAllByHospitalIdAndRole(hospital.getId(), Role.DOCTOR, "").forEach((doctor) -> {
            final List<Appointment> allByDurationAndDateAndTime = appointmentRepository.findAllByDurationAndDateTimeAndDoctor(dateTime, dateTime.plus(15, ChronoUnit.MINUTES), doctor);
            if (allByDurationAndDateAndTime.isEmpty()) {
                final Appointment appointment = new Appointment();
                appointment.setDoctor(doctor);
                appointment.setDateAndTime(dateTime);
                appointment.setDuration(15);
                appointment.setAppointmentStatus(AppointmentStatus.OPEN);
                appointment.setHospital(hospital);
                appointmentRepository.save(appointment);
            }
        }));
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
