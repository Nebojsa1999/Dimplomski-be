package com.isa.domain.api;

import com.isa.config.Principal;
import com.isa.domain.dto.*;
import com.isa.domain.dto.AppointmentResponseDTO;
import com.isa.domain.model.*;
import com.isa.enums.AppointmentStatus;
import com.isa.enums.Role;
import com.isa.exception.NotFoundException;
import com.isa.service.*;
import com.isa.util.PdfReportGenerator;
import com.isa.util.PrescriptionPdfReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
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
    public AppointmentApi(AppointmentService appointmentService,
                          AppointmentReportService appointmentReportService,
                          HospitalService hospitalService,
                          UserService userService,
                          MedicationService medicationService,
                          OperationRoomBookingService operationRoomBookingService,
                          RoomService roomService,
                          FeedbackService feedbackService) {
        this.appointmentService = appointmentService;
        this.appointmentReportService = appointmentReportService;
        this.hospitalService = hospitalService;
        this.userService = userService;
        this.medicationService = medicationService;
        this.operationRoomBookingService = operationRoomBookingService;
        this.roomService = roomService;
        this.feedbackService = feedbackService;
    }

    @GetMapping("api/appointments")
    public ResponseEntity<Page<AppointmentResponseDTO>> page(
            @AuthenticationPrincipal Principal principal,
            @RequestParam(required = false) AppointmentStatus status,
            @RequestParam(required = false) Long from,
            @RequestParam(required = false) Long to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        final User user = userService.get(principal.getUserId()).orElseThrow(NotFoundException::new);
        final Pageable pageable = PageRequest.of(page, size);
        final Instant fromInstant = from != null ? Instant.ofEpochMilli(from) : null;
        final Instant toInstant = to != null ? Instant.ofEpochMilli(to) : null;

        final Page<AppointmentResponseDTO> result = user.getRole() == Role.PATIENT
                ? appointmentService.getPatientAppointments(user, status, fromInstant, toInstant, pageable)
                : appointmentService.getDoctorAppointments(user, status, fromInstant, toInstant, pageable);

        return ResponseEntity.ok(result);
    }

    @GetMapping("api/appointments/open")
    public ResponseEntity<List<OpenSlotDTO>> listSlotsByDoctor(@RequestParam Long doctorId, @RequestParam Long from, @RequestParam Long to) {
        final User doctor = userService.get(doctorId).orElseThrow(NotFoundException::new);
        return ResponseEntity.ok(appointmentService.getOpenByDoctor(doctor, Instant.ofEpochMilli(from), Instant.ofEpochMilli(to)));
    }

    @GetMapping("api/appointments/available")
    public ResponseEntity<List<TimeSlotDTO>> listSlotsByDoctor(@RequestParam Long doctorId, @RequestParam String date) {
        final User doctor = userService.get(doctorId).orElseThrow(NotFoundException::new);
        final LocalDate localDate = LocalDate.parse(date);
        return ResponseEntity.ok(appointmentService.getAvailableTimeSlots(doctor, localDate));
    }

    @PostMapping("api/appointments/book")
    @PreAuthorize("hasAnyAuthority('PATIENT')")
    public ResponseEntity<Appointment> bookAppointment(@RequestBody BookAppointmentDto dto, @AuthenticationPrincipal Principal principal) {

        final User patient = userService.get(principal.getUserId()).orElseThrow(NotFoundException::new);
        final User doctor = userService.get(dto.getDoctorId()).orElseThrow(NotFoundException::new);
        final LocalDate date = LocalDate.parse(dto.getDate());
        final LocalTime startTime = LocalTime.parse(dto.getStartTime());

        final Appointment booked = appointmentService.bookSlot(patient, doctor, date, startTime, dto.getDepartmentProcedureId());
        return new ResponseEntity<>(booked, HttpStatus.CREATED);
    }

    @DeleteMapping("api/appointments/{id}")
    public ResponseEntity<Void> cancelAppointment(@PathVariable long id, @AuthenticationPrincipal Principal principal) {

        final User user = userService.get(principal.getUserId()).orElseThrow(NotFoundException::new);
        if (user.getRole() == Role.PATIENT) {
            appointmentService.cancelByPatient(id, user);
        } else if (user.getRole() == Role.DOCTOR) {
            appointmentService.cancelByDoctor(id, user);
        } else {
            appointmentService.delete(appointmentService.get(id).orElseThrow(NotFoundException::new));
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping("api/appointments/{id}/complete")
    @PreAuthorize("hasAnyAuthority('DOCTOR')")
    public ResponseEntity<AppointmentReport> completeAppointment(@PathVariable long id, @RequestBody AppointmentCompleteDto dto, @AuthenticationPrincipal Principal principal) {

        final User doctor = userService.get(principal.getUserId()).orElseThrow(NotFoundException::new);
        return ResponseEntity.ok(appointmentService.completeAppointment(id, doctor, dto));
    }

    @GetMapping(value = "api/appointments/{id}/report", produces = "application/octet-stream")
    public ResponseEntity<ByteArrayResource> downloadReport(@PathVariable long id) {
        final Appointment appointment = appointmentService.get(id).orElseThrow(NotFoundException::new);
        final AppointmentReport report = appointmentReportService.findByAppointment(appointment).orElseThrow(NotFoundException::new);

        final byte[] bytes = PdfReportGenerator.generatePdf(report);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(bytes.length)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"report-" + id + ".pdf\"")
                .body(new ByteArrayResource(bytes));
    }

    @PostMapping("api/appointments/{id}/room-reservation")
    @PreAuthorize("hasAnyAuthority('DOCTOR', 'ADMIN_SYSTEM')")
    public ResponseEntity<OperationRoomBooking> reserveRoom(@PathVariable long id, @RequestBody RoomReservationDto dto) {

        final Appointment appointment = appointmentService.get(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(operationRoomBookingService.reserve(appointment, dto), HttpStatus.CREATED);
    }

    @PostMapping("api/appointments/{appointmentId}/feedback")
    @PreAuthorize("hasAnyAuthority('PATIENT', 'ADMIN_SYSTEM')")
    public ResponseEntity<Feedback> createFeedback(@PathVariable long appointmentId, @RequestBody FeedbackDto feedbackDto) {

        final Appointment appointment = appointmentService.get(appointmentId).orElseThrow(NotFoundException::new);
        final Feedback feedback = feedbackService.create(appointment, feedbackDto);

        final Hospital hospital = appointment.getDoctor().getHospital();
        if (hospital != null) {
            hospitalService.recalculateRating(hospital);
        }

        return new ResponseEntity<>(feedback, HttpStatus.CREATED);
    }

    @GetMapping("api/appointments/{appointmentId}/feedback")
    public ResponseEntity<Feedback> getFeedback(@PathVariable long appointmentId) {
        final Appointment appointment = appointmentService.get(appointmentId).orElseThrow(NotFoundException::new);
        return ResponseEntity.ok(feedbackService.findByAppointment(appointment).orElseThrow(NotFoundException::new));
    }

    @GetMapping("api/doctors/{doctorId}/rating")
    public ResponseEntity<DoctorRatingDto> getDoctorRating(@PathVariable long doctorId) {
        final User doctor = userService.get(doctorId).orElseThrow(NotFoundException::new);
        final double avg = feedbackService.getAverageRatingForDoctor(doctor);
        final String name = doctor.getFirstName() + " " + doctor.getLastName();
        return ResponseEntity.ok(new DoctorRatingDto(doctorId, name, avg, 0));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @GetMapping("api/hospitals/appointments")
    public ResponseEntity<List<Appointment>> listAll(@RequestParam(required = false) AppointmentStatus appointmentStatus, @RequestParam(required = false) Long from, @RequestParam(required = false) Long to) {
        return ResponseEntity.ok(appointmentService.list(appointmentStatus, from != null ? Instant.ofEpochMilli(from) : null, to != null ? Instant.ofEpochMilli(to) : null));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'DOCTOR', 'PATIENT')")
    @GetMapping("api/hospitals/{id}/appointments")
    public ResponseEntity<List<Appointment>> listByHospital(
            @PathVariable long id,
            @RequestParam(required = false) AppointmentStatus appointmentStatus,
            @RequestParam(required = false) Long from,
            @RequestParam(required = false) Long to) {

        final Hospital hospital = hospitalService.get(id).orElseThrow(NotFoundException::new);
        return ResponseEntity.ok(appointmentService.listByHospital(appointmentStatus, from != null ? Instant.ofEpochMilli(from) : null, to != null ? Instant.ofEpochMilli(to) : null, hospital));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @GetMapping("api/hospitals/appointments/{id}")
    public ResponseEntity<List<Appointment>> getFreeForHospital(@PathVariable long id) {
        final Hospital hospital = hospitalService.get(id).orElseThrow(NotFoundException::new);
        return ResponseEntity.ok(appointmentService.getFreeAppointments(hospital));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @PostMapping("api/hospitals/create-appointment")
    public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        return new ResponseEntity<>(appointmentService.create(appointmentDTO), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'DOCTOR')")
    @GetMapping("api/hospitals/appointment/{id}")
    public ResponseEntity<Appointment> getAppointment(@PathVariable long id) {
        return ResponseEntity.ok(appointmentService.get(id).orElseThrow(NotFoundException::new));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'DOCTOR')")
    @PostMapping("api/hospitals/appointments/{id}/appointment-report")
    public ResponseEntity<AppointmentReport> createAppointmentReport(@RequestBody AppointmentReportDto appointmentReportDto, @PathVariable long id) {
        final Appointment appointment = appointmentService.get(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(appointmentReportService.create(appointmentReportDto, appointment), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'DOCTOR')")
    @PostMapping("api/hospitals/appointments/{id}/medication")
    public ResponseEntity<Medication> createMedication(@RequestBody MedicationDto dto, @PathVariable long id) {
        final Appointment appointment = appointmentService.get(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(medicationService.create(dto, appointment), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @PostMapping("api/hospitals/appointments-date")
    public ResponseEntity<List<Appointment>> getAppointmentsFromDate(@RequestBody AppointmentDateDto appointmentDateDto) {
        final Hospital hospital = hospitalService.get(appointmentDateDto.getDoctorId()).orElseThrow(NotFoundException::new);
        return ResponseEntity.ok(appointmentService.getScheduledAndNotFinishedAppointmentsBasedOnDate(hospital, appointmentDateDto.getDateAndTime()));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @GetMapping("api/hospitals/scheduled-appointments/{id}")
    public ResponseEntity<List<Appointment>> getScheduledForHospital(@PathVariable long id, @RequestParam(required = false) AppointmentStatus appointmentStatus) {
        final Hospital hospital = hospitalService.get(id).orElseThrow(NotFoundException::new);
        return ResponseEntity.ok(appointmentService.getAppointmentsForHospital(hospital, appointmentStatus));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @PostMapping("api/hospitals/appointments/{id}/operation-room-booking")
    public ResponseEntity<OperationRoomBooking> createBooking(@PathVariable long id, @RequestBody RoomReservationDto dto) {
        final Appointment appointment = appointmentService.get(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(operationRoomBookingService.reserve(appointment, dto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @GetMapping("api/hospitals/appointments/room/{id}/operation-room-booking")
    public ResponseEntity<List<OperationRoomBooking>> getBooking(@PathVariable long id) {
        final Room room = roomService.get(id).orElseThrow(NotFoundException::new);
        return ResponseEntity.ok(operationRoomBookingService.findByRoom(room));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'DOCTOR', 'PATIENT')")
    @GetMapping(value = "api/hospitals/appointments/{id}/appointment-report/download", produces = "application/octet-stream")
    public ResponseEntity<ByteArrayResource> downloadAppointmentReport(@PathVariable long id) {
        final Appointment appointment = appointmentService.get(id).orElseThrow(NotFoundException::new);
        final AppointmentReport report = appointmentReportService.findByAppointment(appointment).orElseThrow(NotFoundException::new);
        final byte[] bytes = PdfReportGenerator.generatePdf(report);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(bytes.length)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + report.getAppointment().getDateAndTime() + ".pdf\"")
                .body(new ByteArrayResource(bytes));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'DOCTOR', 'PATIENT')")
    @GetMapping(value = "api/hospitals/appointments/{id}/medication/download", produces = "application/octet-stream")
    public ResponseEntity<ByteArrayResource> downloadPrescription(@PathVariable long id) {
        final Appointment appointment = appointmentService.get(id).orElseThrow(NotFoundException::new);
        final Medication medication = medicationService.findByAppointment(appointment).orElseThrow(NotFoundException::new);
        final byte[] bytes = PrescriptionPdfReport.generatePdf(medication);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(bytes.length)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + medication.getAppointment().getDateAndTime() + ".pdf\"")
                .body(new ByteArrayResource(bytes));
    }
}
