package com.isa.domain.model;

import com.isa.enums.BloodType;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
public class AppointmentReport extends AbstractEntity {

    @Enumerated(EnumType.STRING)
    private BloodType bloodType;

    private String anamnesis;

    private String allergies;

    private String chronicDiseases;

    private String bloodPressure;

    private String hearthRate;

    private String diagnosis;

    private String therapy;

    private String nextControl;

    @Column(columnDefinition = "TEXT")
    private String labResults;

    @Column(columnDefinition = "TEXT")
    private String doctorsComment;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public BloodType getBloodType() {
        return bloodType;
    }

    public void setBloodType(BloodType bloodType) {
        this.bloodType = bloodType;
    }

    public String getAnamnesis() {
        return anamnesis;
    }

    public void setAnamnesis(String anamnesis) {
        this.anamnesis = anamnesis;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getChronicDiseases() {
        return chronicDiseases;
    }

    public void setChronicDiseases(String chronicDiseases) {
        this.chronicDiseases = chronicDiseases;
    }

    public String getNextControl() {
        return nextControl;
    }

    public void setNextControl(String nextControl) {
        this.nextControl = nextControl;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public String getHearthRate() {
        return hearthRate;
    }

    public void setHearthRate(String hearthRate) {
        this.hearthRate = hearthRate;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTherapy() {
        return therapy;
    }

    public void setTherapy(String therapy) {
        this.therapy = therapy;
    }

    public String getLabResults() {
        return labResults;
    }

    public void setLabResults(String labResults) {
        this.labResults = labResults;
    }

    public String getDoctorsComment() {
        return doctorsComment;
    }

    public void setDoctorsComment(String doctorsComment) {
        this.doctorsComment = doctorsComment;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("bloodType", bloodType)
                .append("anamnesis", anamnesis)
                .append("allergies", allergies)
                .append("chronicDiseases", chronicDiseases)
                .append("bloodPressure", bloodPressure)
                .append("hearthRate", hearthRate)
                .append("diagnosis", diagnosis)
                .append("therapy", therapy)
                .append("nextControl", nextControl)
                .append("labResults", labResults)
                .append("doctorsComment", doctorsComment)
                .toString();
    }
}
