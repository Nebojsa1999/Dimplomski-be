package com.isa.domain.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AppointmentReportDto {

    private String anamnesis;

    private String allergies;

    private String chronicDiseases;

    private String bloodPressure;

    private String hearthRate;

    private String diagnosis;

    private String longThermTherapy;

    private String nextControl;

    private String labResults;

    private String doctorsComment;

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

    public String getLongThermTherapy() {
        return longThermTherapy;
    }

    public void setLongThermTherapy(String longThermTherapy) {
        this.longThermTherapy = longThermTherapy;
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
                .append("anamnesis", anamnesis)
                .append("allergies", allergies)
                .append("chronicDiseases", chronicDiseases)
                .append("bloodPressure", bloodPressure)
                .append("hearthRate", hearthRate)
                .append("diagnosis", diagnosis)
                .append("longThermTherapy", longThermTherapy)
                .append("nextControl", nextControl)
                .append("labResults", labResults)
                .append("doctorsComment", doctorsComment)
                .toString();
    }
}
