package com.isa.domain.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class OpenSlotDTO {

    private final Long doctorId;
    private final String doctorName;
    private final LocalDate date;
    private final LocalTime startTime;
    private final LocalTime endTime;

    public OpenSlotDTO(Long doctorId, String doctorName, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}
