package com.isa.domain.dto;

import com.isa.domain.model.PatientMedicalRecord;
import com.isa.domain.model.User;
import com.isa.enums.BloodType;
import com.isa.enums.Gender;

public class PatientMedicalRecordResponseDTO {

    private final Long id;

    // Patient info
    private final Long patientId;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String city;
    private final String country;
    private final String phone;
    private final String personalId;
    private final Gender gender;
    private final String occupation;
    private final String occupationInfo;
    private final double points;

    // Medical record info
    private final BloodType bloodType;
    private final String rhFactor;
    private final Double heightCm;
    private final Double weightKg;
    private final String chronicDiseases;
    private final String previousHospitalization;
    private final String previousSurgeries;
    private final String familyHistory;
    private final String allergies;
    private final String longThermTherapy;
    private final String specificContradictions;

    public PatientMedicalRecordResponseDTO(PatientMedicalRecord record, User patient) {
        this.id = record.getId();
        this.patientId = patient.getId();
        this.email = patient.getEmail();
        this.firstName = patient.getFirstName();
        this.lastName = patient.getLastName();
        this.address = patient.getAddress();
        this.city = patient.getCity();
        this.country = patient.getCountry();
        this.phone = patient.getPhone();
        this.personalId = patient.getPersonalId();
        this.gender = patient.getGender();
        this.occupation = patient.getOccupation();
        this.occupationInfo = patient.getOccupationInfo();
        this.points = patient.getPoints();
        this.bloodType = record.getBloodType();
        this.rhFactor = record.getRhFactor();
        this.heightCm = record.getHeightCm();
        this.weightKg = record.getWeightKg();
        this.chronicDiseases = record.getChronicDiseases();
        this.previousHospitalization = record.getPreviousHospitalization();
        this.previousSurgeries = record.getPreviousSurgeries();
        this.familyHistory = record.getFamilyHistory();
        this.allergies = record.getAllergies();
        this.longThermTherapy = record.getLongThermTherapy();
        this.specificContradictions = record.getSpecificContradictions();
    }

    public Long getId() { return id; }
    public Long getPatientId() { return patientId; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public String getCountry() { return country; }
    public String getPhone() { return phone; }
    public String getPersonalId() { return personalId; }
    public Gender getGender() { return gender; }
    public String getOccupation() { return occupation; }
    public String getOccupationInfo() { return occupationInfo; }
    public double getPoints() { return points; }
    public BloodType getBloodType() { return bloodType; }
    public String getRhFactor() { return rhFactor; }
    public Double getHeightCm() { return heightCm; }
    public Double getWeightKg() { return weightKg; }
    public String getChronicDiseases() { return chronicDiseases; }
    public String getPreviousHospitalization() { return previousHospitalization; }
    public String getPreviousSurgeries() { return previousSurgeries; }
    public String getFamilyHistory() { return familyHistory; }
    public String getAllergies() { return allergies; }
    public String getLongThermTherapy() { return longThermTherapy; }
    public String getSpecificContradictions() { return specificContradictions; }
}
