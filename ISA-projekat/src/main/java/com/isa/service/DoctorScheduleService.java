package com.isa.service;

import com.isa.domain.model.DoctorSchedule;
import com.isa.repository.DoctorScheduleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoctorScheduleService {

    private final DoctorScheduleRepository doctorScheduleRepository;

    @Autowired
    public DoctorScheduleService(DoctorScheduleRepository doctorScheduleRepository) {
        this.doctorScheduleRepository = doctorScheduleRepository;
    }

    @Transactional
    public DoctorSchedule create(DoctorSchedule dto) {
        final DoctorSchedule doctorSchedule = new DoctorSchedule();
        doctorSchedule.setDoctor(dto.getDoctor());
        doctorSchedule.setStartDate(dto.getStartDate());
        doctorSchedule.setStartTime(dto.getStartTime());
        doctorSchedule.setEndTime(dto.getEndTime());
        doctorSchedule.setDayOfWeek(dto.getDayOfWeek());
        doctorSchedule.setDurationOfAppointmentMin(dto.getDurationOfAppointmentMin());
        doctorSchedule.setBreakStartTime(dto.getBreakStartTime());
        doctorSchedule.setBreakEndTime(dto.getBreakEndTime());

        return doctorScheduleRepository.save(doctorSchedule);
    }
}
