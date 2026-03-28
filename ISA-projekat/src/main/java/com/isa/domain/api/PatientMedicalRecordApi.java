package com.isa.domain.api;

import com.isa.domain.dto.PatientMedicalRecordDTO;
import com.isa.domain.dto.PatientMedicalRecordResponseDTO;
import com.isa.domain.model.PatientMedicalRecord;
import com.isa.domain.model.User;
import com.isa.enums.Role;
import com.isa.exception.NotFoundException;
import com.isa.service.PatientMedicalRecordService;
import com.isa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@PreAuthorize("isAuthenticated()")
public class PatientMedicalRecordApi {

    private final PatientMedicalRecordService patientMedicalRecordService;
    private final UserService userService;

    @Autowired
    public PatientMedicalRecordApi(PatientMedicalRecordService patientMedicalRecordService,
                                   UserService userService) {
        this.patientMedicalRecordService = patientMedicalRecordService;
        this.userService = userService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'DOCTOR')")
    @PostMapping("/{patientId}/medical-record")
    public ResponseEntity<PatientMedicalRecord> create(@PathVariable long patientId,
                                                       @RequestBody PatientMedicalRecordDTO dto) {
        final User patient = userService.get(patientId).orElseThrow(NotFoundException::new);
        if (!Role.PATIENT.equals(patient.getRole())) {
            throw new IllegalArgumentException("Medical records can only be created for users with role PATIENT.");
        }
        return new ResponseEntity<>(patientMedicalRecordService.create(dto, patient), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'DOCTOR', 'PATIENT')")
    @GetMapping("/medical-records/{id}")
    public ResponseEntity<PatientMedicalRecord> get(@PathVariable Long id) {
        final PatientMedicalRecord record = patientMedicalRecordService.get(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(record, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'DOCTOR', 'PATIENT')")
    @GetMapping("/{patientId}/medical-record")
    public ResponseEntity<PatientMedicalRecordResponseDTO> getByPatient(@PathVariable long patientId) {
        final User patient = userService.get(patientId).orElseThrow(NotFoundException::new);
        final PatientMedicalRecord record = patientMedicalRecordService.getByPatient(patient.getId()).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(new PatientMedicalRecordResponseDTO(record, patient), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'DOCTOR')")
    @PutMapping("/medical-records/{id}")
    public ResponseEntity<PatientMedicalRecord> update(@PathVariable Long id,
                                                       @RequestBody PatientMedicalRecordDTO dto) {
        final PatientMedicalRecord record = patientMedicalRecordService.get(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(patientMedicalRecordService.update(record, dto), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @DeleteMapping("/medical-records/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        final PatientMedicalRecord record = patientMedicalRecordService.get(id).orElseThrow(NotFoundException::new);
        patientMedicalRecordService.delete(record);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
