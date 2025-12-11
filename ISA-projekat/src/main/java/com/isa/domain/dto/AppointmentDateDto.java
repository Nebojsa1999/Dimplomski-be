package com.isa.domain.dto;

public class AppointmentDateDto {

    private Long adminOfCenterId;
    private String dateAndTime;

    public Long getAdminOfCenterId() {
        return adminOfCenterId;
    }

    public void setAdminOfCenterId(Long adminOfCenterId) {
        this.adminOfCenterId = adminOfCenterId;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }
}
