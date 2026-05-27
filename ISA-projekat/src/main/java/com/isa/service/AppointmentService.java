package com.isa.service;

import com.isa.domain.dto.AppointmentCompleteDto;
import com.isa.domain.dto.AppointmentDTO;
import com.isa.domain.dto.AppointmentResponseDTO;
import com.isa.domain.dto.OpenSlotDTO;
import com.isa.domain.dto.TimeSlotDTO;
import com.isa.domain.model.*;
import com.isa.enums.AppointmentStatus;
import com.isa.exception.NotFoundException;
import com.isa.exception.UnauthorizedException;
import com.isa.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private static final ZoneId ZONE = ZoneId.of("Europe/Belgrade");

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final AppointmentReportRepository appointmentReportRepository;
    private final DiagnosisRepository diagnosisRepository;
    private final DoctorScheduleRepository doctorScheduleRepository;
    private final DepartmentProcedureRepository departmentProcedureRepository;
    private final MedicationRepository medicationRepository;
    private final LabDocumentRepository labDocumentRepository;
    private final FeedbackRepository feedbackRepository;
    private final EmailService emailService;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository,
                               UserRepository userRepository,
                               AppointmentReportRepository appointmentReportRepository,
                               DiagnosisRepository diagnosisRepository,
                               DoctorScheduleRepository doctorScheduleRepository,
                               DepartmentProcedureRepository departmentProcedureRepository,
                               MedicationRepository medicationRepository,
                               LabDocumentRepository labDocumentRepository,
                               FeedbackRepository feedbackRepository,
                              EmailService emailService) {
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.appointmentReportRepository = appointmentReportRepository;
        this.diagnosisRepository = diagnosisRepository;
        this.doctorScheduleRepository = doctorScheduleRepository;
        this.departmentProcedureRepository = departmentProcedureRepository;
        this.medicationRepository = medicationRepository;
        this.labDocumentRepository = labDocumentRepository;
        this.feedbackRepository = feedbackRepository;
        this.emailService = emailService;
    }

    @Transactional
    public Appointment create(AppointmentDTO appointmentDTO) {
        final Appointment appointment = new Appointment();
        appointment.setDuration(Integer.parseInt(appointmentDTO.getDuration()));
        appointment.setDoctor(userRepository.findById(appointmentDTO.getDoctorId()).orElseThrow(NotFoundException::new));
        final Instant instant = Instant.parse(appointmentDTO.getDateAndTime());
        appointment.setDateAndTime(instant);
        appointment.setAppointmentStatus(AppointmentStatus.SCHEDULED);
        return appointmentRepository.save(appointment);
    }

    public List<TimeSlotDTO> getAvailableTimeSlots(User doctor, LocalDate date) {
        final Instant dayInstant = date.atStartOfDay(ZONE).toInstant();
        final List<DoctorSchedule> schedules = doctorScheduleRepository.findActiveForDoctorOnDay(doctor.getId(), date.getDayOfWeek(), dayInstant);

        if (schedules.isEmpty()) {
            return List.of();
        }

        final DoctorSchedule schedule = schedules.get(0);
        final List<TimeSlotDTO> allSlots = generateSlots(schedule);

        final Instant dayStart = date.atStartOfDay(ZONE).toInstant();
        final Instant dayEnd = date.plusDays(1).atStartOfDay(ZONE).toInstant();
        final List<Appointment> booked = appointmentRepository.findConflictingForDoctor(doctor.getId(), dayStart, dayEnd);

        return allSlots.stream()
                .filter(slot -> booked.stream().noneMatch(appointment -> slotConflicts(slot, appointment)))
                .toList();
    }

    private boolean slotConflicts(TimeSlotDTO slot, Appointment appointment) {
        final LocalTime startTime = appointment.getDateAndTime().atZone(ZONE).toLocalTime();
        final LocalTime endTime = startTime.plusMinutes(appointment.getDuration());
        return startTime.isBefore(slot.getEndTime()) && endTime.isAfter(slot.getStartTime());
    }

    private List<TimeSlotDTO> generateSlots(DoctorSchedule schedule) {
        final List<TimeSlotDTO> slots = new java.util.ArrayList<>();
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

    @Transactional
    public Appointment bookSlot(User patient, User doctor, LocalDate date, LocalTime startTime, Long departmentProcedureId) {
        if (date.isBefore(LocalDate.now(ZONE))) {
            throw new IllegalArgumentException("Cannot book an appointment in the past.");
        }

        final Instant dayInstant = date.atStartOfDay(ZONE).toInstant();
        final List<DoctorSchedule> schedules = doctorScheduleRepository.findActiveForDoctorOnDay(
                doctor.getId(), date.getDayOfWeek(), dayInstant);

        if (schedules.isEmpty()) {
            throw new IllegalArgumentException("Doctor does not work on the selected day.");
        }

        final DoctorSchedule schedule = schedules.get(0);
        final int duration = schedule.getDurationOfAppointmentMin();
        final LocalTime slotEnd = getLocalTime(startTime, duration, schedule);

        final Instant slotStartInstant = date.atTime(startTime).atZone(ZONE).toInstant();
        final Instant dayStart = date.atStartOfDay(ZONE).toInstant();
        final Instant dayEnd = date.plusDays(1).atStartOfDay(ZONE).toInstant();

        final boolean hasConflict = appointmentRepository.findConflictingForDoctor(doctor.getId(), dayStart, dayEnd).stream().anyMatch(a -> slotConflicts(new TimeSlotDTO(startTime, slotEnd), a));
        if (hasConflict) {
            throw new IllegalArgumentException("This time slot is no longer available.");
        }

        final Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setDateAndTime(slotStartInstant);
        appointment.setDuration(duration);
        appointment.setAppointmentStatus(AppointmentStatus.SCHEDULED);

        if (departmentProcedureId != null) {
            departmentProcedureRepository.findById(departmentProcedureId).ifPresent(appointment::setDepartmentProcedure);
        }

        final Appointment saved = appointmentRepository.save(appointment);
        emailService.sendAppointmentScheduledEmail(patient.getEmail(), saved);
        return saved;
    }

    @NonNull
    private static LocalTime getLocalTime(LocalTime startTime, int duration, DoctorSchedule schedule) {
        final LocalTime slotEnd = startTime.plusMinutes(duration);

        if (startTime.isBefore(schedule.getStartTime()) || slotEnd.isAfter(schedule.getEndTime())) {
            throw new IllegalArgumentException("Selected time is outside the doctor's working hours.");
        }
        final LocalTime breakStart = schedule.getBreakStartTime();
        final LocalTime breakEnd = schedule.getBreakEndTime();
        if (breakStart != null && breakEnd != null && startTime.isBefore(breakEnd) && slotEnd.isAfter(breakStart)) {
            throw new IllegalArgumentException("Selected time overlaps with the doctor's break.");
        }
        return slotEnd;
    }

    @Transactional
    public void cancelByPatient(long appointmentId, User patient) {
        final Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(NotFoundException::new);

        if (appointment.getPatient() == null || !appointment.getPatient().getId().equals(patient.getId())) {
            throw new UnauthorizedException();
        }
        if (appointment.getAppointmentStatus() != AppointmentStatus.SCHEDULED) {
            throw new IllegalArgumentException("Only SCHEDULED appointments can be cancelled.");
        }

        patient.setPoints(patient.getPoints() - 1);
        userRepository.save(patient);

        appointment.setPatient(null);
        appointment.setAppointmentStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);

        emailService.sendAppointmentCancelledEmail(patient.getEmail(), appointment);
    }

    @Transactional
    public void cancelByDoctor(long appointmentId, User doctor) {
        final Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(NotFoundException::new);

        if (!appointment.getDoctor().getId().equals(doctor.getId())) {
            throw new UnauthorizedException();
        }
        if (appointment.getAppointmentStatus() == AppointmentStatus.FINISHED) {
            throw new IllegalArgumentException("Cannot cancel a finished appointment.");
        }

        final User patient = appointment.getPatient();
        appointmentRepository.delete(appointment);

        if (patient != null) {
            emailService.sendAppointmentCancelledEmail(patient.getEmail(), appointment);
        }
    }

    @Transactional
    public AppointmentReport completeAppointment(long appointmentId, User doctor, AppointmentCompleteDto dto) {
        final Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(NotFoundException::new);

        if (!appointment.getDoctor().getId().equals(doctor.getId())) {
            throw new UnauthorizedException();
        }
        if (appointment.getAppointmentStatus() != AppointmentStatus.SCHEDULED) {
            throw new IllegalArgumentException("Only SCHEDULED appointments can be completed.");
        }

        appointment.setAppointmentStatus(AppointmentStatus.FINISHED);
        appointmentRepository.save(appointment);

        final AppointmentReport report = new AppointmentReport();
        report.setAppointment(appointment);
        report.setBloodPressure(dto.getBloodPressure());
        report.setHearthRate(dto.getPulse());
        report.setLabResults(dto.getLabResults());
        report.setTherapy(dto.getTherapy());
        report.setBloodType(dto.getBloodType());
        report.setAllergies(dto.getAllergies());
        report.setAnamnesis(dto.getAnamnesis());
        report.setChronicDiseases(dto.getChronicDiseases());
        report.setNextControl(dto.getNextControl());

        if (dto.getDiagnosisId() != null) {
            final Diagnosis diagnosis = diagnosisRepository.findById(dto.getDiagnosisId()).orElseThrow(() -> new NotFoundException("Diagnosis not found"));
            report.setDiagnosis(diagnosis.getName() + " [" + diagnosis.getCode() + "]");
        }

        return appointmentReportRepository.save(report);
    }

    public Page<AppointmentResponseDTO> getPatientAppointments(User patient, AppointmentStatus status, Instant from, Instant to, Pageable pageable) {
        return appointmentRepository.findByPatient(patient.getId(), status, from, to, pageable).map(this::toResponseDTO);
    }

    public Page<AppointmentResponseDTO> getDoctorAppointments(User doctor, AppointmentStatus status, Instant from, Instant to, Pageable pageable) {
        return appointmentRepository.findByDoctor(doctor.getId(), status, from, to, pageable).map(this::toResponseDTO);
    }

    private AppointmentResponseDTO toResponseDTO(Appointment appointment) {
        final Long id = appointment.getId();
        return AppointmentResponseDTO.from(appointment, appointmentReportRepository.existsByAppointmentId(id), medicationRepository.existsByAppointmentId(id), labDocumentRepository.existsByAppointmentId(id), feedbackRepository.existsByAppointmentId(id));
    }

    public List<Appointment> list(AppointmentStatus appointmentStatus, Instant from, Instant to) {
        return appointmentRepository.findAll(appointmentStatus, from, to);
    }

    public List<Appointment> listByHospital(AppointmentStatus appointmentStatus, Instant from, Instant to, Hospital hospital) {
        return appointmentRepository.findAllByHospitalId(appointmentStatus, from, to, hospital.getId());
    }

    public List<OpenSlotDTO> getOpenByDoctor(User doctor, Instant from, Instant to) {
        final LocalDate dateFrom = from.atZone(ZoneOffset.UTC).toLocalDate();
        final LocalDate dateTo = to.atZone(ZoneOffset.UTC).toLocalDate();

        final List<OpenSlotDTO> result = new java.util.ArrayList<>();
        LocalDate current = dateFrom;
        while (!current.isAfter(dateTo)) {
            final LocalDate date = current;
            getAvailableTimeSlots(doctor, date).forEach(slot ->
                    result.add(new OpenSlotDTO(
                            doctor.getId(),
                            doctor.getFirstName() + " " + doctor.getLastName(),
                            date,
                            slot.getStartTime(),
                            slot.getEndTime())));
            current = current.plusDays(1);
        }
        return result;
    }

    public List<Appointment> getFreeAppointments(Hospital hospital) {
        return appointmentRepository.findAllByDoctorHospitalId(hospital.getId()).stream()
                .filter(a -> a.getPatient() == null
                        && a.getAppointmentStatus() == AppointmentStatus.SCHEDULED
                        && a.getDateAndTime().isAfter(Instant.now()))
                .toList();
    }

    public List<Appointment> getAppointmentsForHospital(Hospital hospital, AppointmentStatus appointmentStatus) {
        return appointmentRepository.findAllByDoctorHospitalId(hospital.getId()).stream()
                .filter(a -> a.getPatient() != null && appointmentStatus == a.getAppointmentStatus())
                .toList();
    }

    public List<Appointment> getMonthlyCalendar(User doctor, int year, int month) {
        final YearMonth ym = YearMonth.of(year, month);
        final Instant monthStart = ym.atDay(1).atStartOfDay(ZONE).toInstant();
        final Instant monthEnd = ym.atEndOfMonth().plusDays(1).atStartOfDay(ZONE).toInstant();
        return appointmentRepository.findByDoctorAndMonth(doctor.getId(), monthStart, monthEnd);
    }

    public List<Appointment> getScheduledAndNotFinishedAppointmentsBasedOnDate(Hospital hospital, String date) {
        final DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-M-d");
        final LocalDate ldt = LocalDate.parse(date, f);

        return appointmentRepository.findAllByDoctorHospitalId(hospital.getId()).stream()
                .filter(a -> a.getPatient() != null
                        && a.getAppointmentStatus() == AppointmentStatus.SCHEDULED
                        && a.getDateAndTime().atOffset(ZoneOffset.UTC).toLocalDate().equals(ldt))
                .toList();
    }

    public Optional<Appointment> get(long id) {
        return appointmentRepository.findById(id);
    }

    public void save(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    public void delete(Appointment appointment) {
        appointmentRepository.delete(appointment);
    }

    @Transactional
    public void penalisePatient(Appointment appointment) {
        final User patient = appointment.getPatient();
        if (patient != null) {
            patient.setPoints(patient.getPoints() - 1);
            userRepository.save(patient);
        }
        appointment.setPatient(null);
        appointment.setAppointmentStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }
}
