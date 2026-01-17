package com.isa.domain.dto;

import com.isa.domain.model.Medication;
import com.isa.domain.model.Prescription;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PrescriptionDto {

    private Prescription prescription;
    private Medication medication;

    public Medication getMedication() {
        return medication;
    }

    public void setMedication(Medication medication) {
        this.medication = medication;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("prescription", prescription)
                .append("medication", medication)
                .toString();
    }
}
