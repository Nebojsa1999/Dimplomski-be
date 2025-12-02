package com.isa.domain.dto;

public class AppointmentDateDto {

    private Long centerAccountId;
    private String date;

    public Long getCenterAccountId() {
        return centerAccountId;
    }

    public void setCenterAccountId(Long centerAccountId) {
        this.centerAccountId = centerAccountId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
