package com.isa.process;

import com.isa.enums.AppointmentStatus;
import com.isa.service.AppointmentService;
import com.isa.service.OperationRoomBookingService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Component
public class DeleteAndAddAppointmentsConfig {

    private final AppointmentService appointmentService;
    private final OperationRoomBookingService operationRoomBookingService;

    @Autowired
    public DeleteAndAddAppointmentsConfig(AppointmentService appointmentService, OperationRoomBookingService operationRoomBookingService) {
        this.appointmentService = appointmentService;
        this.operationRoomBookingService = operationRoomBookingService;
    }

    @PostConstruct
    public void deleteAppointmentsInPast() {
        appointmentService.deleteFromPast(Instant.now(), AppointmentStatus.OPEN);
        operationRoomBookingService.deleteFromPast(Instant.now());
    }

    @PostConstruct
    public void addAppointmentTomorrow() {
        final ZoneId BELGRADE = ZoneId.of("Europe/Belgrade");
        final LocalDate today = LocalDate.now(BELGRADE).plusDays(1);
        final Instant tenAM = today.atTime(10, 0)
                .atZone(BELGRADE)
                .toInstant();

        final Instant elevenAM = today.atTime(11, 0)
                .atZone(BELGRADE)
                .toInstant();

        final Instant twelvePM = today.atTime(12, 0)
                .atZone(BELGRADE)
                .toInstant();

        appointmentService.addAppointments(tenAM);
        appointmentService.addAppointments(elevenAM);
        appointmentService.addAppointments(twelvePM);
    }
}
