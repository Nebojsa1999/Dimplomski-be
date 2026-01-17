package com.isa.domain.api;

import com.isa.config.Principal;
import com.isa.domain.dto.AppointmentDTO;
import com.isa.domain.dto.AppointmentDateDto;
import com.isa.domain.dto.AppointmentReportDto;
import com.isa.domain.dto.DenyUserDto;
import com.isa.domain.model.Appointment;
import com.isa.domain.model.AppointmentReport;
import com.isa.domain.model.Hospital;
import com.isa.domain.model.User;
import com.isa.enums.AppointmentStatus;
import com.isa.exception.NotFoundException;
import com.isa.service.AppointmentReportService;
import com.isa.service.AppointmentService;
import com.isa.service.HospitalService;
import com.isa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/hospitals")
@PreAuthorize("isAuthenticated()")
public class AppointmentApi {

    private final AppointmentService appointmentService;
    private final AppointmentReportService appointmentReportService;
    private final HospitalService hospitalService;
    private final UserService userService;

    @Autowired
    public AppointmentApi(AppointmentService appointmentService, AppointmentReportService appointmentReportService, HospitalService hospitalService, UserService userService) {
        this.appointmentService = appointmentService;
        this.appointmentReportService = appointmentReportService;
        this.hospitalService = hospitalService;
        this.userService = userService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @GetMapping(path = "/appointments/{id}")
    public ResponseEntity<List<Appointment>> getAppointments(@PathVariable long id) {
        final Hospital hospital = hospitalService.get(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(appointmentService.getFreeAppointments(hospital), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @PostMapping("/create-appointment")
    public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        final Appointment appointment = appointmentService.create(appointmentDTO);
        return new ResponseEntity<>(appointment, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @GetMapping("/scheduled-appointments/{id}")
    public ResponseEntity<List<Appointment>> getAppointmentsForHospital(@PathVariable long id, @RequestParam(required = false) AppointmentStatus appointmentStatus) {
        final Hospital hospital = hospitalService.get(id).orElseThrow(NotFoundException::new);
        final List<Appointment> appointments = appointmentService.getAppointmentsForHospital(hospital, appointmentStatus);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @PutMapping("/deny-user")
    public ResponseEntity<Void> deny(@RequestBody DenyUserDto denyUserDto) {
        final Appointment appointment = appointmentService.get(Long.parseLong(denyUserDto.getId())).orElseThrow(NotFoundException::new);
        userService.lowerUserPoints(appointment);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @GetMapping(path = "/appointment/{id}")
    public ResponseEntity<Appointment> getAppointment(@PathVariable long id) {
        final Appointment appointment = appointmentService.get(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @PostMapping(path = "/appointment-report")
    public ResponseEntity<AppointmentReport> createAppointmentReport(@RequestBody AppointmentReportDto appointmentReportDto, @AuthenticationPrincipal Principal principal) {
        final User user = userService.get(principal.getUserId()).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(appointmentReportService.create(appointmentReportDto, user), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @PostMapping(path = "/appointments-date")
    public ResponseEntity<List<Appointment>> getAppointmentsFromDate(@RequestBody AppointmentDateDto appointmentDateDto) {
        final Hospital hospital = hospitalService.get(appointmentDateDto.getDoctorId()).orElseThrow(NotFoundException::new);
        final List<Appointment> appointments = appointmentService.getScheduledAndNotFinishedAppointmentsBasedOnDate(hospital, appointmentDateDto.getDateAndTime());
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }
}
