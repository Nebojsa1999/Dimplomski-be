package com.isa.service;

import com.isa.domain.dto.DoctorScheduleDTO;
import com.isa.domain.dto.DoctorScheduleDayDTO;
import com.isa.domain.dto.TimeSlotDTO;
import com.isa.domain.model.DoctorSchedule;
import com.isa.domain.model.User;
import com.isa.repository.DoctorScheduleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorScheduleService {

    private final DoctorScheduleRepository doctorScheduleRepository;

    @Autowired
    public DoctorScheduleService(DoctorScheduleRepository doctorScheduleRepository) {
        this.doctorScheduleRepository = doctorScheduleRepository;
    }

    @Transactional
    public List<DoctorSchedule> create(DoctorScheduleDTO dto, User doctor) {
        return dto.getDays().stream()
                .map(day -> {
                    final List<DoctorSchedule> overlapping = doctorScheduleRepository.findOverlapping(
                            doctor.getId(), day.getDayOfWeek(), day.getStartTime(), day.getEndTime(), 0L);
                    if (!overlapping.isEmpty()) {
                        throw new IllegalArgumentException(
                                "Schedule for " + day.getDayOfWeek() + " overlaps with an existing schedule for this doctor.");
                    }
                    final DoctorSchedule schedule = new DoctorSchedule();
                    schedule.setDoctor(doctor);
                    schedule.setStartDate(dto.getStartDate());
                    schedule.setEndDate(dto.getEndDate());
                    schedule.setDayOfWeek(day.getDayOfWeek());
                    schedule.setStartTime(day.getStartTime());
                    schedule.setEndTime(day.getEndTime());
                    schedule.setDurationOfAppointmentMin(day.getDurationOfAppointmentMin());
                    schedule.setBreakStartTime(day.getBreakStartTime());
                    schedule.setBreakEndTime(day.getBreakEndTime());
                    return doctorScheduleRepository.save(schedule);
                })
                .toList();
    }

    public Optional<DoctorSchedule> get(Long id) {
        return doctorScheduleRepository.findById(id);
    }

    public List<DoctorSchedule> list(Long doctorId) {
        return doctorId != null
                ? doctorScheduleRepository.findAllByDoctorId(doctorId)
                : doctorScheduleRepository.findAll();
    }

    public List<DoctorSchedule> listInDateRange(Long doctorId, Instant startDate, Instant endDate) {
        return doctorScheduleRepository.findAllInDateRange(doctorId, startDate, endDate);
    }

    @Transactional
    public DoctorSchedule update(DoctorSchedule schedule, DoctorScheduleDayDTO dto) {
        final DayOfWeek dayOfWeek = dto.getDayOfWeek() != null ? dto.getDayOfWeek() : schedule.getDayOfWeek();
        final List<DoctorSchedule> overlapping = doctorScheduleRepository.findOverlapping(
                schedule.getDoctor().getId(), dayOfWeek, dto.getStartTime(), dto.getEndTime(), schedule.getId());
        if (!overlapping.isEmpty()) {
            throw new IllegalArgumentException(
                    "Schedule for " + dayOfWeek + " overlaps with an existing schedule for this doctor.");
        }
        if (dto.getDayOfWeek() != null) schedule.setDayOfWeek(dto.getDayOfWeek());
        if (dto.getStartTime() != null) schedule.setStartTime(dto.getStartTime());
        if (dto.getEndTime() != null) schedule.setEndTime(dto.getEndTime());
        if (dto.getDurationOfAppointmentMin() != null) schedule.setDurationOfAppointmentMin(dto.getDurationOfAppointmentMin());
        schedule.setBreakStartTime(dto.getBreakStartTime());
        schedule.setBreakEndTime(dto.getBreakEndTime());
        return doctorScheduleRepository.save(schedule);
    }

    public void delete(DoctorSchedule schedule) {
        doctorScheduleRepository.delete(schedule);
    }

    public List<TimeSlotDTO> generateSlots(DoctorSchedule schedule, LocalDate date) {
        if (date.getDayOfWeek() != schedule.getDayOfWeek()) {
            return List.of();
        }
        final List<TimeSlotDTO> slots = new ArrayList<>();
        LocalTime current = schedule.getStartTime();
        final int duration = schedule.getDurationOfAppointmentMin();
        final LocalTime scheduleEnd = schedule.getEndTime();
        final LocalTime breakStart = schedule.getBreakStartTime();
        final LocalTime breakEnd = schedule.getBreakEndTime();
        while (!current.plusMinutes(duration).isAfter(scheduleEnd)) {
            final LocalTime slotEnd = current.plusMinutes(duration);
            if (breakStart != null && breakEnd != null
                    && current.isBefore(breakEnd) && slotEnd.isAfter(breakStart)) {
                current = breakEnd;
                continue;
            }
            slots.add(new TimeSlotDTO(current, slotEnd));
            current = slotEnd;
        }
        return slots;
    }
}
