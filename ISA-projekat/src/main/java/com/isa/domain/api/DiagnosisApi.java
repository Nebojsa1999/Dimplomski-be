package com.isa.domain.api;

import com.isa.domain.dto.DiagnosisDTO;
import com.isa.domain.model.Department;
import com.isa.domain.model.Diagnosis;
import com.isa.exception.NotFoundException;
import com.isa.service.DepartmentService;
import com.isa.service.DiagnosisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/departments")
@PreAuthorize("isAuthenticated()")
public class DiagnosisApi {

    private final DiagnosisService diagnosisService;
    private final DepartmentService departmentService;

    @Autowired
    public DiagnosisApi(DiagnosisService diagnosisService, DepartmentService departmentService) {
        this.diagnosisService = diagnosisService;
        this.departmentService = departmentService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @PostMapping("/{departmentId}/diagnoses")
    public ResponseEntity<Diagnosis> create(@PathVariable long departmentId,
                                            @RequestBody DiagnosisDTO dto) {
        final Department department = departmentService.get(departmentId).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(diagnosisService.create(dto, department), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @GetMapping("/diagnoses/{id}")
    public ResponseEntity<Diagnosis> get(@PathVariable Long id) {
        final Diagnosis diagnosis = diagnosisService.get(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(diagnosis, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @GetMapping("/diagnoses")
    public ResponseEntity<List<Diagnosis>> list(@RequestParam(required = false) Long departmentId) {
        return new ResponseEntity<>(diagnosisService.list(departmentId), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @PutMapping("/diagnoses/{id}")
    public ResponseEntity<Diagnosis> update(@PathVariable Long id, @RequestBody DiagnosisDTO dto) {
        final Diagnosis diagnosis = diagnosisService.get(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(diagnosisService.update(diagnosis, dto), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @DeleteMapping("/diagnoses/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        final Diagnosis diagnosis = diagnosisService.get(id).orElseThrow(NotFoundException::new);
        diagnosisService.delete(diagnosis);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
