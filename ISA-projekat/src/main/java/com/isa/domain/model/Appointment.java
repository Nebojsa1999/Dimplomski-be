package com.isa.domain.model;

import com.isa.enums.AppointmentStatus;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.Instant;

@Entity
public class Appointment extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "hospital_id")
    private Hospital hospital; //hospital has more appointments manyToOne

    private Instant dateAndTime;

    private int duration;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private User doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private User patient;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus appointmentStatus;

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public Instant getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(Instant dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public User getDoctor() {
        return doctor;
    }

    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }

    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }

    public AppointmentStatus getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(AppointmentStatus appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("hospital", hospital)
                .append("dateAndTime", dateAndTime)
                .append("duration", duration)
                .append("doctor", doctor)
                .append("patient", patient)
                .append("appointmentStatus", appointmentStatus)
                .toString();
    }
}
