package com.isa.domain.api;

import com.isa.domain.dto.DiagnosisDTO;
import com.isa.domain.model.Diagnosis;
import com.isa.exception.NotFoundException;
import com.isa.service.DiagnosisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/diagnoses")
@PreAuthorize("isAuthenticated()")
public class DiagnosisApi {

    private final DiagnosisService diagnosisService;

    @Autowired
    public DiagnosisApi(DiagnosisService diagnosisService) {
        this.diagnosisService = diagnosisService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @PostMapping
    public ResponseEntity<Diagnosis> create(@RequestBody DiagnosisDTO dto) {
        return new ResponseEntity<>(diagnosisService.create(dto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @GetMapping("/{id}")
    public ResponseEntity<Diagnosis> get(@PathVariable Long id) {
        final Diagnosis diagnosis = diagnosisService.get(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(diagnosis, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'DOCTOR')")
    @GetMapping
    public ResponseEntity<List<Diagnosis>> list(@RequestParam(required = false) String name, @RequestParam(required = false) String departmentName) {
        return new ResponseEntity<>(diagnosisService.list(name, departmentName), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @PutMapping("/{id}")
    public ResponseEntity<Diagnosis> update(@PathVariable Long id, @RequestBody DiagnosisDTO dto) {
        final Diagnosis diagnosis = diagnosisService.get(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(diagnosisService.update(diagnosis, dto), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        final Diagnosis diagnosis = diagnosisService.get(id).orElseThrow(NotFoundException::new);
        diagnosisService.delete(diagnosis);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
