package com.isa.domain.api;

import com.isa.config.Principal;
import com.isa.domain.dto.*;
import com.isa.domain.model.*;
import com.isa.exception.NotFoundException;
import com.isa.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/center")
@PreAuthorize("isAuthenticated()")
public class CenterAccountApi {

    private final CenterAccountService centerAccountService;
    private final UserService userService;
    private final AppointmentService appointmentService;
    private final BloodService bloodService;

    private final AppointmentReportService appointmentReportService;

    private final EquipmentService equipmentService;

    @Autowired
    public CenterAccountApi(CenterAccountService centerAccountService, UserService userService, AppointmentService appointmentService, BloodService bloodService, AppointmentReportService appointmentReportService, EquipmentService equipmentService) {
        this.centerAccountService = centerAccountService;
        this.userService = userService;
        this.appointmentService = appointmentService;
        this.bloodService = bloodService;
        this.appointmentReportService = appointmentReportService;
        this.equipmentService = equipmentService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @PutMapping
    public ResponseEntity<CenterAccount> update(@RequestBody @Valid CenterAccountDto update, @AuthenticationPrincipal Principal principal) {
        final User user = userService.get(principal.getUserId()).orElseThrow(NotFoundException::new);
        final CenterAccount centerAccount = centerAccountService.get(user.getCenterAccount().getId()).orElseThrow(NotFoundException::new);
        final CenterAccount updated = centerAccountService.update(centerAccount, update);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @GetMapping
    public ResponseEntity<CenterAccount> get(@AuthenticationPrincipal Principal principal) {
        final User user = userService.get(principal.getUserId()).orElseThrow(NotFoundException::new);
        final CenterAccount centerAccount = centerAccountService.get(user.getCenterAccount().getId()).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(centerAccount, HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<List<CenterAccount>> list(@RequestBody SearchDto searchDto) {
        final List<CenterAccount> list = centerAccountService.list(searchDto.getCenterName());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @GetMapping(path = "/admins-of-center/{id}")
    public ResponseEntity<List<User>> getAdminsOfCenter(@PathVariable long id) {
        final CenterAccount centerAccount = centerAccountService.get(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(userService.getAllByCenterAccount(centerAccount), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @GetMapping(path = "/appointments/{id}")
    public ResponseEntity<List<Appointment>> getAppointments(@PathVariable long id) {
        final CenterAccount centerAccount = centerAccountService.get(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(appointmentService.getFreeAppointments(centerAccount), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @GetMapping("/{id}/bloodSample")
    public ResponseEntity<List<BloodSample>> getBloodSampleInCenterAccount(@PathVariable long id) {
        final CenterAccount centerAccount = centerAccountService.get(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(bloodService.getBloodSampleByCenterAccount(centerAccount), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @PostMapping("/create-appointment")
    public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        final Appointment appointment = appointmentService.create(appointmentDTO);
        return new ResponseEntity<>(appointment, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @GetMapping("/scheduled-appointments/{id}")
    public ResponseEntity<List<Appointment>> getAppointmentsForCenterAccount(@PathVariable long id, @RequestParam(required = false) boolean completed) {
        final CenterAccount centerAccount = centerAccountService.get(id).orElseThrow(NotFoundException::new);
        final List<Appointment> appointments = appointmentService.getAppointmentsForCenterAccount(centerAccount, completed);
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
    @GetMapping(path = "/equipments")
    public ResponseEntity<List<Equipment>> getEquipments() {
        return new ResponseEntity<>(equipmentService.getAll(), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @PostMapping(path = "/appointments-date")
    public ResponseEntity<List<Appointment>> getAppointmentsFromDate(@RequestBody AppointmentDateDto appointmentDateDto)  {
        final CenterAccount centerAccount = centerAccountService.get(appointmentDateDto.getAdminOfCenterId()).orElseThrow(NotFoundException::new);
        final List<Appointment> appointments = appointmentService.getScheduledAndNotFinishedAppointmentsBasedOnDate(centerAccount, appointmentDateDto.getDateAndTime());
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }
}
