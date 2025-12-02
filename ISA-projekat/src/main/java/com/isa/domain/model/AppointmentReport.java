package com.isa.domain.model;

import com.isa.enums.BloodType;

import jakarta.persistence.*;

@Entity
public class AppointmentReport extends AbstractEntity{

    @Enumerated(EnumType.STRING)
    private BloodType bloodType;

    private double bloodAmount;

    private String noteToDoctor;

    private String copperSulfate;

    private String hemoglobinometer;

    private String lungs;

    private String heart;

    private String TA;

    private String TT;

    private String TV;

    private String bagType;

    private String note;

    private String punctureSite;

    private String startOfGiving;

    private String endOfGiving;

    private String reasonForPrematureTerminationOfBloodDonation;

    private boolean denied;

    private String reasonForDenying;

    @OneToOne
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    private double equipmentAmount;

    public BloodType getBloodType() {
        return bloodType;
    }

    public void setBloodType(BloodType bloodType) {
        this.bloodType = bloodType;
    }

    public double getBloodAmount() {
        return bloodAmount;
    }

    public void setBloodAmount(double bloodAmount) {
        this.bloodAmount = bloodAmount;
    }

    public String getNoteToDoctor() {
        return noteToDoctor;
    }

    public void setNoteToDoctor(String noteToDoctor) {
        this.noteToDoctor = noteToDoctor;
    }

    public String getCopperSulfate() {
        return copperSulfate;
    }

    public void setCopperSulfate(String copperSulfate) {
        this.copperSulfate = copperSulfate;
    }

    public String getHemoglobinometer() {
        return hemoglobinometer;
    }

    public void setHemoglobinometer(String hemoglobinometer) {
        this.hemoglobinometer = hemoglobinometer;
    }

    public String getLungs() {
        return lungs;
    }

    public void setLungs(String lungs) {
        this.lungs = lungs;
    }

    public String getHeart() {
        return heart;
    }

    public void setHeart(String heart) {
        this.heart = heart;
    }

    public String getTA() {
        return TA;
    }

    public void setTA(String TA) {
        this.TA = TA;
    }

    public String getTT() {
        return TT;
    }

    public void setTT(String TT) {
        this.TT = TT;
    }

    public String getTV() {
        return TV;
    }

    public void setTV(String TV) {
        this.TV = TV;
    }

    public String getBagType() {
        return bagType;
    }

    public void setBagType(String bagType) {
        this.bagType = bagType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPunctureSite() {
        return punctureSite;
    }

    public void setPunctureSite(String punctureSite) {
        this.punctureSite = punctureSite;
    }

    public String getStartOfGiving() {
        return startOfGiving;
    }

    public void setStartOfGiving(String startOfGiving) {
        this.startOfGiving = startOfGiving;
    }

    public String getEndOfGiving() {
        return endOfGiving;
    }

    public void setEndOfGiving(String endOfGiving) {
        this.endOfGiving = endOfGiving;
    }

    public String getReasonForPrematureTerminationOfBloodDonation() {
        return reasonForPrematureTerminationOfBloodDonation;
    }

    public void setReasonForPrematureTerminationOfBloodDonation(String reasonForPrematureTerminationOfBloodDonation) {
        this.reasonForPrematureTerminationOfBloodDonation = reasonForPrematureTerminationOfBloodDonation;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public double getEquipmentAmount() {
        return equipmentAmount;
    }

    public void setEquipmentAmount(double equipmentAmount) {
        this.equipmentAmount = equipmentAmount;
    }

    public boolean isDenied() {
        return denied;
    }

    public void setDenied(boolean denied) {
        this.denied = denied;
    }

    public String getReasonForDenying() {
        return reasonForDenying;
    }

    public void setReasonForDenying(String reasonForDenying) {
        this.reasonForDenying = reasonForDenying;
    }
}
