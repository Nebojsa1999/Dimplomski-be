package com.isa.service;

import com.isa.domain.dto.AppointmentReportDto;
import com.isa.domain.model.Appointment;
import com.isa.domain.model.AppointmentReport;
import com.isa.repository.AppointmentReportRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppointmentReportService {

    private final AppointmentReportRepository appointmentReportRepository;


    @Autowired
    public AppointmentReportService(AppointmentReportRepository appointmentReportRepository) {
        this.appointmentReportRepository = appointmentReportRepository;
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
        appointmentReport.setAppointment(appointment);

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
