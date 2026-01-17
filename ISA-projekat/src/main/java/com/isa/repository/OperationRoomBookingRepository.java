package com.isa.repository;

import com.isa.domain.model.OperationRoomBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationRoomBookingRepository extends JpaRepository<OperationRoomBooking, Long> {
}
