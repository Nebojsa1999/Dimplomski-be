package com.isa.domain.api;

import com.isa.config.Principal;
import com.isa.domain.dto.*;
import com.isa.domain.model.*;
import com.isa.enums.Role;
import com.isa.enums.RoomType;
import com.isa.exception.NotFoundException;
import com.isa.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/hospitals")
@PreAuthorize("isAuthenticated()")
public class HospitalApi {

    private final HospitalService hospitalService;
    private final UserService userService;

    private final EquipmentService equipmentService;
    private final RoomService roomService;
    private final OperationRoomBookingService operationRoomBookingService;

    @Autowired
    public HospitalApi(HospitalService hospitalService, UserService userService, EquipmentService equipmentService, RoomService roomService, OperationRoomBookingService operationRoomBookingService) {
        this.hospitalService = hospitalService;
        this.userService = userService;
        this.equipmentService = equipmentService;
        this.roomService = roomService;
        this.operationRoomBookingService = operationRoomBookingService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @PostMapping
    public ResponseEntity<Hospital> create(@RequestBody HospitalDto create) {
        return new ResponseEntity<>(hospitalService.create(create), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @PutMapping("/{id}")
    public ResponseEntity<Hospital> update(@PathVariable Long id, @RequestBody @Valid HospitalDto update, @AuthenticationPrincipal Principal principal) {
        final Hospital hospital = hospitalService.get(id).orElseThrow(NotFoundException::new);
        final Hospital updated = hospitalService.update(hospital, update);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @GetMapping("/{id}")
    public ResponseEntity<Hospital> get(@PathVariable Long id, @AuthenticationPrincipal Principal principal) {
        final Hospital hospital = hospitalService.get(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(hospital, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @GetMapping
    public ResponseEntity<List<Hospital>> list(@RequestParam(required = false) String name) {
        final List<Hospital> list = hospitalService.list(name);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @GetMapping(path = "/equipments")
    public ResponseEntity<List<Equipment>> getEquipments(@RequestParam(required = false) String name) {
        return new ResponseEntity<>(equipmentService.getAll(name), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @PostMapping(path = "/equipment")
    public ResponseEntity<Equipment> createEquipment(@RequestBody Equipment equipmentDto) {
        return new ResponseEntity<>(equipmentService.create(equipmentDto), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @GetMapping(path = "/equipment/{id}")
    public ResponseEntity<Equipment> getEquipment(@PathVariable Long id) {
        final Equipment equipment = equipmentService.get(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(equipment, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @PutMapping(path = "/equipment/{id}")
    public ResponseEntity<Equipment> updateEquipment(@PathVariable Long id, @RequestBody Equipment equipmentDto) {
        final Equipment equipment = equipmentService.get(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(equipmentService.update(equipment, equipmentDto), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @GetMapping(path = "/{id}/equipments")
    public ResponseEntity<List<Equipment>> getEquipmentsByHospitalId(@PathVariable Long id, @RequestParam(required = false) String name) {
        return new ResponseEntity<>(equipmentService.findByHospitalId(id, name), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @GetMapping(path = "/room/{id}")
    public ResponseEntity<Room> getRoom(@PathVariable Long id) {
        final Room room = roomService.get(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(room, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @PostMapping(path = "/room")
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        return new ResponseEntity<>(roomService.create(room), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @PutMapping(path = "/room/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long id, @RequestBody Room roomDto) {
        final Room room = roomService.get(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(roomService.update(roomDto, room), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @GetMapping(path = "/rooms")
    public ResponseEntity<List<Room>> listRooms(@RequestParam(required = false) String name) {
        return new ResponseEntity<>(roomService.list(name), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @GetMapping(path = "/{id}/users")
    public ResponseEntity<List<User>> getUsersFromHospital(@PathVariable long id, @RequestParam(required = false) Role role, @RequestParam(required = false) String name) {
        final Hospital hospital = hospitalService.get(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(userService.getAllByHospital(hospital, role, name), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @PostMapping(path = "/operation-room-booking")
    public ResponseEntity<OperationRoomBooking> createBooking(@RequestBody OperationRoomBooking operationRoomBooking) {
        return new ResponseEntity<>(operationRoomBookingService.create(operationRoomBooking), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @GetMapping(path = "/{id}/free-rooms")
    public ResponseEntity<List<Room>> availableRooms(@PathVariable Long id, @RequestParam(required = false) String name, @RequestParam(required = false) RoomType roomType) {
        final Hospital hospital = hospitalService.get(id).orElseThrow(NotFoundException::new);
        final List<Room> byHospitalAndFree = roomService.findByHospitalAndFree(hospital, roomType, name, null, null);

        return new ResponseEntity<>(byHospitalAndFree, HttpStatus.OK);
    }
}
