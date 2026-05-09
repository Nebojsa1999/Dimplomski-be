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

    @Query("""
            SELECT b FROM OperationRoomBooking b
            WHERE b.room.id = :roomId
              AND b.startTime < :end
              AND b.endTime > :start
            """)
    List<OperationRoomBooking> findOverlapping(
            @Param("roomId") Long roomId,
            @Param("start") Instant start,
            @Param("end") Instant end);
}
