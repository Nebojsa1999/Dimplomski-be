package com.isa.repository;

import com.isa.domain.model.Hospital;
import com.isa.domain.model.Room;
import com.isa.enums.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("""
            select room
                        FROM Room room
                                    WHERE :searchFilter IS NULL OR :searchFilter = '' OR  LOWER(room.roomNumber) LIKE LOWER(CONCAT('%', :searchFilter, '%'))
            """)
    List<Room> findAllByName(@Param("searchFilter") String searchFilter);

    @Query("""
    SELECT r
    FROM Room r
    WHERE r.hospital = :hospital
      AND (:type is null or r.type = :type)
      AND (
           :searchFilter IS NULL
           OR :searchFilter = ''
           OR LOWER(r.roomNumber) LIKE LOWER(CONCAT('%', :searchFilter, '%'))
      )
      AND (
           :startTime IS NULL
           OR :endTime IS NULL
           OR NOT EXISTS (
               SELECT 1
               FROM OperationRoomBooking b
               WHERE b.room = r
                 AND b.startTime < :endTime
                 AND b.endTime > :startTime
           )
      )
""")
    List<Room> findAvailableRooms(
            @Param("hospital") Hospital hospital,
            @Param("type") RoomType type,
            @Param("searchFilter") String searchFilter,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}
