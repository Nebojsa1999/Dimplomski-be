package com.isa.repository;

import com.isa.domain.model.OperationRoomBooking;
import com.isa.domain.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface OperationRoomBookingRepository extends JpaRepository<OperationRoomBooking, Long> {

    List<OperationRoomBooking> findAllByRoom(Room room);

    @Query("SELECT operationRoomBooking FROM OperationRoomBooking operationRoomBooking WHERE operationRoomBooking.endTime < :timestamp")
    List<OperationRoomBooking> findAllInThePast(@Param("timestamp") Instant timestamp);
}
