package com.isa.domain.model;

import com.isa.enums.DayOfWeek;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.Instant;
import java.time.LocalTime;

@Entity
public class DoctorSchedule extends AbstractEntity{

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private User doctor;

    private Instant startDate;

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

    public User getDoctor() {
        return doctor;
    }

    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
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

    public void setEndTime(LocalTime endtime) {
        this.endTime = endtime;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("dayOfWeek", dayOfWeek)
                .append("doctor", doctor)
                .append("startDate", startDate)
                .append("startTime", startTime)
                .append("endtime", endTime)
                .append("durationOfAppointmentMin", durationOfAppointmentMin)
                .append("breakStartTime", breakStartTime)
                .append("breakEndTime", breakEndTime)
                .toString();
    }
}
