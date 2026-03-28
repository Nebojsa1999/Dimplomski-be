package com.isa.domain.dto;

import java.time.Instant;
import java.util.List;

public class DoctorScheduleDTO {

    private Instant startDate;
    private Instant endDate;
    private List<DoctorScheduleDayDTO> days;

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public List<DoctorScheduleDayDTO> getDays() {
        return days;
    }

    public void setDays(List<DoctorScheduleDayDTO> days) {
        this.days = days;
    }
}
