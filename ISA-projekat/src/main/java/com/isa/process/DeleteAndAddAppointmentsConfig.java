package com.isa.process;

import com.isa.enums.AppointmentStatus;
import com.isa.service.AppointmentService;
import com.isa.service.OperationRoomBookingService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;

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
}
