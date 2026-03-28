package com.isa.domain.dto;

import com.isa.enums.BloodType;

public class PatientMedicalRecordDTO {

    private BloodType bloodType;
    private String rhFactor;
    private Double heightCm;
    private Double weightKg;
    private String chronicDiseases;
    private String previousHospitalization;
    private String previousSurgeries;
    private String familyHistory;
    private String allergies;
    private String longThermTherapy;
    private String specificContradictions;
    private Long patientId;

    public BloodType getBloodType() {
        return bloodType;
    }

    public void setBloodType(BloodType bloodType) {
        this.bloodType = bloodType;
    }

    public String getRhFactor() {
        return rhFactor;
    }

    public void setRhFactor(String rhFactor) {
        this.rhFactor = rhFactor;
    }

    public Double getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(Double heightCm) {
        this.heightCm = heightCm;
    }

    public Double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(Double weightKg) {
        this.weightKg = weightKg;
    }

    public String getChronicDiseases() {
        return chronicDiseases;
    }

    public void setChronicDiseases(String chronicDiseases) {
        this.chronicDiseases = chronicDiseases;
    }

    public String getPreviousHospitalization() {
        return previousHospitalization;
    }

    public void setPreviousHospitalization(String previousHospitalization) {
        this.previousHospitalization = previousHospitalization;
    }

    public String getPreviousSurgeries() {
        return previousSurgeries;
    }

    public void setPreviousSurgeries(String previousSurgeries) {
        this.previousSurgeries = previousSurgeries;
    }

    public String getFamilyHistory() {
        return familyHistory;
    }

    public void setFamilyHistory(String familyHistory) {
        this.familyHistory = familyHistory;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getLongThermTherapy() {
        return longThermTherapy;
    }

    public void setLongThermTherapy(String longThermTherapy) {
        this.longThermTherapy = longThermTherapy;
    }

    public String getSpecificContradictions() {
        return specificContradictions;
    }

    public void setSpecificContradictions(String specificContradictions) {
        this.specificContradictions = specificContradictions;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
}
