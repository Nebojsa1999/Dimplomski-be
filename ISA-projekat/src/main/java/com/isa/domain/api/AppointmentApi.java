package com.isa.domain.api;

import com.isa.config.Principal;
import com.isa.domain.dto.*;
import com.isa.domain.model.*;
import com.isa.enums.AppointmentStatus;
import com.isa.enums.DoctorType;
import com.isa.exception.NotFoundException;
import com.isa.service.*;
import com.isa.util.PdfReportGenerator;
import com.isa.util.PrescriptionPdfReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("api/hospitals")
@PreAuthorize("isAuthenticated()")
public class AppointmentApi {

    private final AppointmentService appointmentService;
    private final AppointmentReportService appointmentReportService;
    private final HospitalService hospitalService;
    private final UserService userService;
    private final MedicationService medicationService;
    private final OperationRoomBookingService operationRoomBookingService;
    private final RoomService roomService;
    private final FeedbackService feedbackService;

    @Autowired
    public AppointmentApi(AppointmentService appointmentService, AppointmentReportService appointmentReportService, HospitalService hospitalService, UserService userService, MedicationService medicationService, OperationRoomBookingService operationRoomBookingService, RoomService roomService, FeedbackService feedbackService, FeedbackService feedbackService1) {
        this.appointmentService = appointmentService;
        this.appointmentReportService = appointmentReportService;
        this.hospitalService = hospitalService;
        this.userService = userService;
        this.medicationService = medicationService;
        this.operationRoomBookingService = operationRoomBookingService;
        this.roomService = roomService;
        this.feedbackService = feedbackService1;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @GetMapping(path = "/appointments")
    public ResponseEntity<List<Appointment>> list(@RequestParam(required = false) AppointmentStatus appointmentStatus, @RequestParam(required = false) Long from, @RequestParam(required = false) Long to) {
        return new ResponseEntity<>(appointmentService.list(appointmentStatus, from != null ? Instant.ofEpochMilli(from) : null, to != null ? Instant.ofEpochMilli(to) : null), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'DOCTOR', 'PATIENT')")
    @GetMapping(path = "/{id}/appointments")
    public ResponseEntity<List<Appointment>> listByHospitalId(@PathVariable long id, @RequestParam(required = false) AppointmentStatus appointmentStatus, @RequestParam(required = false) Long from, @RequestParam(required = false) Long to, @RequestParam(required = false) DoctorType doctorType) {
        final Hospital hospital = hospitalService.get(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(appointmentService.listByHospital(appointmentStatus, from != null ? Instant.ofEpochMilli(from) : null, to != null ? Instant.ofEpochMilli(to) : null, hospital, doctorType), HttpStatus.OK);
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

    @PutMapping("/appointments/{id}/schedule")
    public ResponseEntity<Appointment> schedule(@PathVariable long id, @AuthenticationPrincipal Principal principal) {
        final User user = userService.get(principal.getUserId()).orElseThrow(NotFoundException::new);
        final Appointment appointment = appointmentService.get(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(appointmentService.schedule(appointment, user), HttpStatus.OK);
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

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'DOCTOR')")
    @GetMapping(path = "/appointment/{id}")
    public ResponseEntity<Appointment> getAppointment(@PathVariable long id) {
        final Appointment appointment = appointmentService.get(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(appointment, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'DOCTOR')")
    @PostMapping(path = "/appointments/{id}/appointment-report")
    public ResponseEntity<AppointmentReport> createAppointmentReport(@RequestBody AppointmentReportDto appointmentReportDto, @PathVariable long id, @AuthenticationPrincipal Principal principal) {
        final Appointment appointment = appointmentService.get(id).orElseThrow(NotFoundException::new);
        final User user = userService.get(principal.getUserId()).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(appointmentReportService.create(appointmentReportDto, appointment), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'DOCTOR')")
    @PostMapping(path = "/appointments/{id}/medication")
    public ResponseEntity<Medication> createMedication(@RequestBody Medication medication, @PathVariable long id, @AuthenticationPrincipal Principal principal) {
        final Appointment appointment = appointmentService.get(id).orElseThrow(NotFoundException::new);
        final User user = userService.get(principal.getUserId()).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(medicationService.create(medication, appointment), HttpStatus.CREATED);
    }


    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @PostMapping(path = "/appointments-date")
    public ResponseEntity<List<Appointment>> getAppointmentsFromDate(@RequestBody AppointmentDateDto appointmentDateDto) {
        final Hospital hospital = hospitalService.get(appointmentDateDto.getDoctorId()).orElseThrow(NotFoundException::new);
        final List<Appointment> appointments = appointmentService.getScheduledAndNotFinishedAppointmentsBasedOnDate(hospital, appointmentDateDto.getDateAndTime());
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }


    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @PostMapping(path = "/appointments/{id}/operation-room-booking")
    public ResponseEntity<OperationRoomBooking> createBooking(@PathVariable long id, @RequestBody OperationBookingDto dto) {
        final Appointment appointment = appointmentService.get(id).orElseThrow(NotFoundException::new);
        final OperationRoomBooking operationRoomBooking = operationRoomBookingService.create(appointment, dto);

        roomService.removeCapacity(dto.getRoom());
        return new ResponseEntity<>(operationRoomBooking, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @GetMapping(path = "/appointments/room/{id}/operation-room-booking")
    public ResponseEntity<List<OperationRoomBooking>> getBooking(@PathVariable long id) {
        final Room room = roomService.get(id).orElseThrow(NotFoundException::new);
        final List<OperationRoomBooking> operationRoomBooking = operationRoomBookingService.findByRoom(room);

        return new ResponseEntity<>(operationRoomBooking, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'PATIENT')")
    @PostMapping(path = "/appointments/{appointmentId}/feedback")
    public ResponseEntity<Feedback> createFeedback(@PathVariable long appointmentId, @RequestBody FeedbackDto feedbackDto) {
        final Appointment appointment = appointmentService.get(appointmentId).orElseThrow(NotFoundException::new);
        final Feedback feedback = feedbackService.create(appointment, feedbackDto);

        return new ResponseEntity<>(feedback, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'PATIENT')")
    @GetMapping(path = "/appointments/{appointmentId}/feedback")
    public ResponseEntity<Feedback> getFeedback(@PathVariable long appointmentId) {
        final Appointment appointment = appointmentService.get(appointmentId).orElseThrow(NotFoundException::new);
        final Feedback feedback = feedbackService.findByAppointment(appointment).orElseThrow(NotFoundException::new);

        return new ResponseEntity<>(feedback, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'DOCTOR')")
    @GetMapping(value = "/appointments/{id}/appointment-report/download", produces = "application/octet-stream")
    public ResponseEntity<ByteArrayResource> downloadAppointmentReport(@PathVariable long id) {
        final Appointment appointment = appointmentService.get(id).orElseThrow(NotFoundException::new);
        final AppointmentReport appointmentReport = appointmentReportService.findByAppointment(appointment).orElseThrow(NotFoundException::new);

        final byte[] bytes = PdfReportGenerator.generatePdf(appointmentReport);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(bytes.length)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + appointmentReport.getAppointment().getDateAndTime() + ".pdf" + "\"")
                .body(new ByteArrayResource(bytes));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'DOCTOR')")
    @GetMapping(value = "/appointments/{id}/medication/download", produces = "application/octet-stream")
    public ResponseEntity<ByteArrayResource> downloadPrescription(@PathVariable long id) {
        final Appointment appointment = appointmentService.get(id).orElseThrow(NotFoundException::new);
        final Medication medication = medicationService.findByAppointment(appointment).orElseThrow(NotFoundException::new);

        final byte[] bytes = PrescriptionPdfReport.generatePdf(medication);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(bytes.length)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + medication.getAppointment().getDateAndTime() + ".pdf" + "\"")
                .body(new ByteArrayResource(bytes));
    }
}
