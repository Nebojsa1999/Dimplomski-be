package com.isa.domain.api;

import com.isa.config.Principal;
import com.isa.domain.dto.TimeSlotDTO;
import com.isa.domain.model.Appointment;
import com.isa.domain.model.FavoriteDoctor;
import com.isa.domain.model.User;
import com.isa.enums.Role;
import com.isa.exception.NotFoundException;
import com.isa.service.AppointmentService;
import com.isa.service.FavoriteDoctorService;
import com.isa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/doctors")
@PreAuthorize("isAuthenticated()")
public class DoctorCalendarApi {

    private final AppointmentService appointmentService;
    private final UserService userService;
    private final FavoriteDoctorService favoriteDoctorService;

    @Autowired
    public DoctorCalendarApi(AppointmentService appointmentService,
                              UserService userService,
                              FavoriteDoctorService favoriteDoctorService) {
        this.appointmentService = appointmentService;
        this.userService = userService;
        this.favoriteDoctorService = favoriteDoctorService;
    }

    @GetMapping("/{doctorId}/calendar")
    public ResponseEntity<List<Appointment>> getMonthlyCalendar(@PathVariable long doctorId, @RequestParam int year, @RequestParam int month) {

        final User doctor = userService.get(doctorId).orElseThrow(NotFoundException::new);
        if (doctor.getRole() != Role.DOCTOR) {
            throw new IllegalArgumentException("User is not a doctor.");
        }
        return ResponseEntity.ok(appointmentService.getMonthlyCalendar(doctor, year, month));
    }

    @GetMapping("/{doctorId}/available")
    public ResponseEntity<List<TimeSlotDTO>> getAvailableSlots(@PathVariable long doctorId, @RequestParam String date) {

        final User doctor = userService.get(doctorId).orElseThrow(NotFoundException::new);
        final LocalDate localDate = LocalDate.parse(date);
        return ResponseEntity.ok(appointmentService.getAvailableTimeSlots(doctor, localDate));
    }

    @GetMapping("/favorites")
    @PreAuthorize("hasAnyAuthority('PATIENT')")
    public ResponseEntity<List<FavoriteDoctor>> getFavorites(@AuthenticationPrincipal Principal principal) {
        final User patient = userService.get(principal.getUserId()).orElseThrow(NotFoundException::new);
        return ResponseEntity.ok(favoriteDoctorService.getFavoritesByPatient(patient));
    }

    @PostMapping("/{doctorId}/favorite")
    @PreAuthorize("hasAnyAuthority('PATIENT')")
    public ResponseEntity<FavoriteDoctor> addFavorite(@PathVariable long doctorId, @AuthenticationPrincipal Principal principal) {

        final User patient = userService.get(principal.getUserId()).orElseThrow(NotFoundException::new);
        final User doctor = userService.get(doctorId).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(favoriteDoctorService.addFavorite(patient, doctor), HttpStatus.CREATED);
    }

    @DeleteMapping("/{doctorId}/favorite")
    @PreAuthorize("hasAnyAuthority('PATIENT')")
    public ResponseEntity<Void> removeFavorite(@PathVariable long doctorId, @AuthenticationPrincipal Principal principal) {

        final User patient = userService.get(principal.getUserId()).orElseThrow(NotFoundException::new);
        final User doctor = userService.get(doctorId).orElseThrow(NotFoundException::new);
        favoriteDoctorService.removeFavorite(patient, doctor);
        return ResponseEntity.noContent().build();
    }
}
