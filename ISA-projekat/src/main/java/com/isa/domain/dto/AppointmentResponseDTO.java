package com.isa.domain.dto;

import com.isa.domain.model.Appointment;
import com.isa.domain.model.DepartmentProcedure;
import com.isa.domain.model.User;
import com.isa.enums.AppointmentStatus;

import java.time.Instant;

public class AppointmentResponseDTO {

    private Long id;
    private Instant dateAndTime;
    private int duration;
    private User doctor;
    private User patient;
    private AppointmentStatus appointmentStatus;
    private DepartmentProcedure departmentProcedure;
    private boolean hasAppointmentReport;
    private boolean hasMedication;
    private boolean hasLabDocument;
    private boolean hasFeedback;

    public static AppointmentResponseDTO from(Appointment appointment,
                                              boolean hasAppointmentReport,
                                              boolean hasMedication,
                                              boolean hasLabDocument,
                                              boolean hasFeedback) {
        final AppointmentResponseDTO dto = new AppointmentResponseDTO();
        dto.id = appointment.getId();
        dto.dateAndTime = appointment.getDateAndTime();
        dto.duration = appointment.getDuration();
        dto.doctor = appointment.getDoctor();
        dto.patient = appointment.getPatient();
        dto.appointmentStatus = appointment.getAppointmentStatus();
        dto.departmentProcedure = appointment.getDepartmentProcedure();
        dto.hasAppointmentReport = hasAppointmentReport;
        dto.hasMedication = hasMedication;
        dto.hasLabDocument = hasLabDocument;
        dto.hasFeedback = hasFeedback;
        return dto;
    }

    public Long getId() { return id; }
    public Instant getDateAndTime() { return dateAndTime; }
    public int getDuration() { return duration; }
    public User getDoctor() { return doctor; }
    public User getPatient() { return patient; }
    public AppointmentStatus getAppointmentStatus() { return appointmentStatus; }
    public DepartmentProcedure getDepartmentProcedure() { return departmentProcedure; }
    public boolean isHasAppointmentReport() { return hasAppointmentReport; }
    public boolean isHasMedication() { return hasMedication; }
    public boolean isHasLabDocument() { return hasLabDocument; }
    public boolean isHasFeedback() { return hasFeedback; }
}
