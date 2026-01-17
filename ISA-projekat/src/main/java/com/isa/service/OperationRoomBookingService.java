package com.isa.service;

import com.isa.domain.model.OperationRoomBooking;
import com.isa.repository.OperationRoomBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OperationRoomBookingService {

    private final OperationRoomBookingRepository operationRoomBookingRepository;

    @Autowired
    public OperationRoomBookingService(OperationRoomBookingRepository operationRoomBookingRepository) {
        this.operationRoomBookingRepository = operationRoomBookingRepository;
    }

    public OperationRoomBooking create(OperationRoomBooking operationRoomBooking) {
        return operationRoomBookingRepository.save(operationRoomBooking);
    }

    public Optional<OperationRoomBooking> get(Long id) {
        return operationRoomBookingRepository.findById(id);
    }

    public void update(OperationRoomBooking operationRoomBookingDto, OperationRoomBooking operationRoomBooking) {
        operationRoomBooking.setOperationType(operationRoomBookingDto.getOperationType());
        operationRoomBooking.setEndTime(operationRoomBookingDto.getEndTime());
        operationRoomBooking.setRoom(operationRoomBookingDto.getRoom());
        operationRoomBooking.setDoctor(operationRoomBookingDto.getDoctor());
        operationRoomBooking.setNotes(operationRoomBookingDto.getNotes());
        operationRoomBooking.setStartTime(operationRoomBookingDto.getStartTime());
        operationRoomBooking.setPatient(operationRoomBookingDto.getPatient());
        operationRoomBookingRepository.save(operationRoomBooking);
    }
}
