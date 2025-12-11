package com.isa.domain.dto;

public class AppointmentDTO {

    private String dateAndTime;
    private String duration;
    private Long adminOfCenterId;

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Long getAdminOfCenterId() {
        return adminOfCenterId;
    }

    public void setAdminOfCenterId(Long adminOfCenterId) {
        this.adminOfCenterId = adminOfCenterId;
    }
}
