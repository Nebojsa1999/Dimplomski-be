package com.isa.service;

import com.isa.domain.dto.RoomReservationDto;
import com.isa.domain.model.Appointment;
import com.isa.domain.model.OperationRoomBooking;
import com.isa.domain.model.Room;
import com.isa.exception.NotFoundException;
import com.isa.repository.OperationRoomBookingRepository;
import com.isa.repository.RoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class OperationRoomBookingService {

    private final OperationRoomBookingRepository operationRoomBookingRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public OperationRoomBookingService(OperationRoomBookingRepository operationRoomBookingRepository,
                                       RoomRepository roomRepository) {
        this.operationRoomBookingRepository = operationRoomBookingRepository;
        this.roomRepository = roomRepository;
    }

    @Transactional
    public OperationRoomBooking reserve(Appointment appointment, RoomReservationDto dto) {
        final Room room = roomRepository.findById(dto.getRoomId()).orElseThrow(NotFoundException::new);
        final Instant start = Instant.parse(dto.getStartTime());
        final Instant end = Instant.parse(dto.getEndTime());

        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("End time must be after start time.");
        }

        final List<OperationRoomBooking> conflicts = operationRoomBookingRepository.findOverlapping(room.getId(), start, end);
        if (!conflicts.isEmpty()) {
            throw new IllegalArgumentException("Room is already reserved for the requested time period.");
        }

        final OperationRoomBooking booking = new OperationRoomBooking();
        booking.setRoom(room);
        booking.setDoctor(appointment.getDoctor());
        booking.setPatient(appointment.getPatient());
        booking.setOperationType(dto.getOperationType());
        booking.setStartTime(start);
        booking.setEndTime(end);

        return operationRoomBookingRepository.save(booking);
    }

    public Optional<OperationRoomBooking> get(Long id) {
        return operationRoomBookingRepository.findById(id);
    }

    public List<OperationRoomBooking> findByRoom(Room room) {
        return operationRoomBookingRepository.findAllByRoom(room);
    }

    public void update(OperationRoomBooking updated, OperationRoomBooking existing) {
        existing.setOperationType(updated.getOperationType());
        existing.setEndTime(updated.getEndTime());
        existing.setRoom(updated.getRoom());
        existing.setDoctor(updated.getDoctor());
        existing.setStartTime(updated.getStartTime());
        existing.setPatient(updated.getPatient());
        operationRoomBookingRepository.save(existing);
    }
}
