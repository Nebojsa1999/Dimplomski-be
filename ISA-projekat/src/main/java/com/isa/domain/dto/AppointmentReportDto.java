package com.isa.domain.dto;

import com.isa.enums.BloodType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AppointmentReportDto {

    @Enumerated(EnumType.STRING)
    private BloodType bloodType;

    private String pastMedicalHistory;

    private String allergies;

    private String familyHistory;

    private String bloodPressure;

    private String hearthRate;

    private String diagnosis;

    public BloodType getBloodType() {
        return bloodType;
    }

    public void setBloodType(BloodType bloodType) {
        this.bloodType = bloodType;
    }

    public String getPastMedicalHistory() {
        return pastMedicalHistory;
    }

    public void setPastMedicalHistory(String pastMedicalHistory) {
        this.pastMedicalHistory = pastMedicalHistory;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getFamilyHistory() {
        return familyHistory;
    }

    public void setFamilyHistory(String familyHistory) {
        this.familyHistory = familyHistory;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("bloodType", bloodType)
                .append("pastMedicalHistory", pastMedicalHistory)
                .append("allergies", allergies)
                .append("familyHistory", familyHistory)
                .append("bloodPressure", bloodPressure)
                .append("hearthRate", hearthRate)
                .append("diagnosis", diagnosis)
                .toString();
    }
}
