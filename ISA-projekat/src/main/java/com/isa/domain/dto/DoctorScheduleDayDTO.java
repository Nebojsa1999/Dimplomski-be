package com.isa.domain.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class DoctorScheduleDayDTO {

    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer durationOfAppointmentMin;
    private LocalTime breakStartTime;
    private LocalTime breakEndTime;

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Integer getDurationOfAppointmentMin() {
        return durationOfAppointmentMin;
    }

    public void setDurationOfAppointmentMin(Integer durationOfAppointmentMin) {
        this.durationOfAppointmentMin = durationOfAppointmentMin;
    }

    public LocalTime getBreakStartTime() {
        return breakStartTime;
    }

    public void setBreakStartTime(LocalTime breakStartTime) {
        this.breakStartTime = breakStartTime;
    }

    public LocalTime getBreakEndTime() {
        return breakEndTime;
    }

    public void setBreakEndTime(LocalTime breakEndTime) {
        this.breakEndTime = breakEndTime;
    }
}
