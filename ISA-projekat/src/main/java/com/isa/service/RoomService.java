package com.isa.service;

import com.isa.domain.model.Hospital;
import com.isa.domain.model.Room;
import com.isa.enums.RoomType;
import com.isa.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room create(Room room) {
        return roomRepository.save(room);
    }

    public Optional<Room> get(Long id) {
        return roomRepository.findById(id);
    }

    public Room update(Room roomDto, Room room) {
        room.setRoomNumber(roomDto.getRoomNumber());
        room.setType(roomDto.getType());
        room.setCapacity(roomDto.getCapacity());
        return roomRepository.save(room);
    }

    public List<Room> list(String name) {
        return name != null ? roomRepository.findAllByName(name) : roomRepository.findAll();
    }

    public List<Room> findByHospitalAndFree(Hospital hospital, RoomType roomType, String name, LocalDateTime start, LocalDateTime end) {
        return roomRepository.findAvailableRooms(hospital, roomType, name, start, end);
    }

    public void removeCapacity(Room room) {
        room.setCapacity(room.getCapacity() - 1);
        roomRepository.save(room);
    }

    public void addCapacity(Room room) {
        room.setCapacity(room.getCapacity() + 1);
        roomRepository.save(room);
    }
}
