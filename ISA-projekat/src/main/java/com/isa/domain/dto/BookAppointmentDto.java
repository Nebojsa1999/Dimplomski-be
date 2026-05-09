package com.isa.domain.dto;

import jakarta.validation.constraints.NotNull;

public class BookAppointmentDto {

    @NotNull
    private Long doctorId;

    @NotNull
    private String date;

    @NotNull
    private String startTime;

    private Long departmentProcedureId;

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Long getDepartmentProcedureId() {
        return departmentProcedureId;
    }

    public void setDepartmentProcedureId(Long departmentProcedureId) {
        this.departmentProcedureId = departmentProcedureId;
    }
}
