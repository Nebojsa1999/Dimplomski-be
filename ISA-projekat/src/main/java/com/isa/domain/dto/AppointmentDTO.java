package com.isa.domain.dto;

public class AppointmentDTO {

    private String date;
    private String duration;
    private Long adminOfCenterId;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
