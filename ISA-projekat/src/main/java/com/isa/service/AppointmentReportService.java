package com.isa.service;

import com.isa.domain.dto.AppointmentReportDto;
import com.isa.domain.model.Appointment;
import com.isa.domain.model.AppointmentReport;
import com.isa.enums.AppointmentStatus;
import com.isa.repository.AppointmentReportRepository;
import com.isa.repository.AppointmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppointmentReportService {

    private final AppointmentReportRepository appointmentReportRepository;
    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentReportService(AppointmentReportRepository appointmentReportRepository, AppointmentRepository appointmentRepository) {
        this.appointmentReportRepository = appointmentReportRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Transactional
    public AppointmentReport create(AppointmentReportDto appointmentReportDto, Appointment appointment) {
        final AppointmentReport appointmentReport = new AppointmentReport();
        appointmentReport.setBloodType(appointmentReportDto.getBloodType());
        appointmentReport.setAllergies(appointmentReportDto.getAllergies());
        appointmentReport.setBloodPressure(appointmentReportDto.getBloodPressure());
        appointmentReport.setFamilyHistory(appointmentReportDto.getFamilyHistory());
        appointmentReport.setPastMedicalHistory(appointmentReportDto.getPastMedicalHistory());
        appointmentReport.setHearthRate(appointmentReportDto.getHearthRate());
        appointmentReport.setDiagnosis(appointmentReportDto.getDiagnosis());
        appointmentReport.setTherapy(appointmentReportDto.getTherapy());
        appointmentReport.setLabResults(appointmentReportDto.getLabResults());
        appointmentReport.setDoctorsComment(appointmentReportDto.getDoctorsComment());

        appointment.setAppointmentStatus(AppointmentStatus.FINISHED);
        appointmentReport.setAppointment(appointment);

        appointmentRepository.save(appointment);
        appointmentReportRepository.save(appointmentReport);
        return appointmentReport;
    }

    public Optional<AppointmentReport> get(AppointmentReport appointmentReport) {
        return appointmentReportRepository.findById(appointmentReport.getId());
    }

    public Optional<AppointmentReport> findByAppointment(Appointment appointment) {
        return appointmentReportRepository.findAppointmentReportsByAppointmentId(appointment.getId());
    }
}
