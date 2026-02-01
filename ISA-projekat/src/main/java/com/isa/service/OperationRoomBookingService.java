package com.isa.service;

import com.isa.domain.dto.OperationBookingDto;
import com.isa.domain.model.Appointment;
import com.isa.domain.model.OperationRoomBooking;
import com.isa.domain.model.Room;
import com.isa.repository.OperationRoomBookingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class OperationRoomBookingService {

    private final OperationRoomBookingRepository operationRoomBookingRepository;
    private final RoomService roomService;

    @Autowired
    public OperationRoomBookingService(OperationRoomBookingRepository operationRoomBookingRepository, RoomService roomService) {
        this.operationRoomBookingRepository = operationRoomBookingRepository;
        this.roomService = roomService;
    }

    public OperationRoomBooking create(Appointment appointment, OperationBookingDto dto) {
        final OperationRoomBooking operationRoomBooking = new OperationRoomBooking();
        operationRoomBooking.setDoctor(appointment.getDoctor());
        operationRoomBooking.setPatient(appointment.getPatient());
        operationRoomBooking.setOperationType(dto.getOperationType());
        operationRoomBooking.setRoom(dto.getRoom());
        operationRoomBooking.setStartTime(Instant.parse(dto.getStartTime()));
        operationRoomBooking.setEndTime(Instant.parse(dto.getEndTime()));
        return operationRoomBookingRepository.save(operationRoomBooking);
    }

    public Optional<OperationRoomBooking> get(Long id) {
        return operationRoomBookingRepository.findById(id);
    }

    public List<OperationRoomBooking> findByRoom(Room room) {
        return operationRoomBookingRepository.findAllByRoom(room);
    }


    @Transactional
    public void deleteFromPast(Instant now) {
        final List<OperationRoomBooking> allInThePast = operationRoomBookingRepository.findAllInThePast(now);
        allInThePast.forEach(booking -> {
            roomService.addCapacity(booking.getRoom());
            operationRoomBookingRepository.delete(booking);
        });
    }

    public void update(OperationRoomBooking operationRoomBookingDto, OperationRoomBooking operationRoomBooking) {
        operationRoomBooking.setOperationType(operationRoomBookingDto.getOperationType());
        operationRoomBooking.setEndTime(operationRoomBookingDto.getEndTime());
        operationRoomBooking.setRoom(operationRoomBookingDto.getRoom());
        operationRoomBooking.setDoctor(operationRoomBookingDto.getDoctor());
        operationRoomBooking.setStartTime(operationRoomBookingDto.getStartTime());
        operationRoomBooking.setPatient(operationRoomBookingDto.getPatient());
        operationRoomBookingRepository.save(operationRoomBooking);
    }
}
