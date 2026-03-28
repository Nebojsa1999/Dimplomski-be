package com.isa.domain.api;

import com.isa.domain.dto.DoctorScheduleDayDTO;
import com.isa.domain.dto.DoctorScheduleDTO;
import com.isa.domain.dto.TimeSlotDTO;
import com.isa.domain.model.DoctorSchedule;
import com.isa.domain.model.User;
import com.isa.enums.Role;
import com.isa.exception.NotFoundException;
import com.isa.service.DoctorScheduleService;
import com.isa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/users")
@PreAuthorize("isAuthenticated()")
public class DoctorScheduleApi {

    private final DoctorScheduleService doctorScheduleService;
    private final UserService userService;

    @Autowired
    public DoctorScheduleApi(DoctorScheduleService doctorScheduleService, UserService userService) {
        this.doctorScheduleService = doctorScheduleService;
        this.userService = userService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @PostMapping("/{doctorId}/schedules")
    public ResponseEntity<List<DoctorSchedule>> create(@PathVariable long doctorId,
                                                       @RequestBody DoctorScheduleDTO dto) {
        final User doctor = userService.get(doctorId).orElseThrow(NotFoundException::new);
        if (!Role.DOCTOR.equals(doctor.getRole())) {
            throw new IllegalArgumentException("Schedules can only be created for users with role DOCTOR.");
        }
        return new ResponseEntity<>(doctorScheduleService.create(dto, doctor), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'DOCTOR', 'PATIENT')")
    @GetMapping("/schedules/{id}")
    public ResponseEntity<DoctorSchedule> get(@PathVariable Long id) {
        final DoctorSchedule schedule = doctorScheduleService.get(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(schedule, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'DOCTOR', 'PATIENT')")
    @GetMapping("/schedules")
    public ResponseEntity<List<DoctorSchedule>> list(@RequestParam(required = false) Long doctorId) {
        return new ResponseEntity<>(doctorScheduleService.list(doctorId), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'DOCTOR', 'PATIENT')")
    @GetMapping("/schedules/range")
    public ResponseEntity<List<DoctorSchedule>> listInDateRange(
            @RequestParam(required = false) Long doctorId,
            @RequestParam Instant startDate,
            @RequestParam Instant endDate) {
        return new ResponseEntity<>(doctorScheduleService.listInDateRange(doctorId, startDate, endDate), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @PutMapping("/schedules/{id}")
    public ResponseEntity<DoctorSchedule> update(@PathVariable Long id,
                                                 @RequestBody DoctorScheduleDayDTO dto) {
        final DoctorSchedule schedule = doctorScheduleService.get(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(doctorScheduleService.update(schedule, dto), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @DeleteMapping("/schedules/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        final DoctorSchedule schedule = doctorScheduleService.get(id).orElseThrow(NotFoundException::new);
        doctorScheduleService.delete(schedule);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'DOCTOR', 'PATIENT')")
    @GetMapping("/schedules/{id}/slots")
    public ResponseEntity<List<TimeSlotDTO>> getSlots(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        final DoctorSchedule schedule = doctorScheduleService.get(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(doctorScheduleService.generateSlots(schedule, date), HttpStatus.OK);
    }
}
