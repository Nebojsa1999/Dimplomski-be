package com.isa.domain.api;

import com.isa.domain.dto.MedicamentDTO;
import com.isa.domain.model.Department;
import com.isa.domain.model.Medicament;
import com.isa.exception.NotFoundException;
import com.isa.service.DepartmentService;
import com.isa.service.MedicamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/departments")
@PreAuthorize("isAuthenticated()")
public class MedicamentApi {

    private final MedicamentService medicamentService;
    private final DepartmentService departmentService;

    @Autowired
    public MedicamentApi(MedicamentService medicamentService, DepartmentService departmentService) {
        this.medicamentService = medicamentService;
        this.departmentService = departmentService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @PostMapping("/{departmentId}/medicaments")
    public ResponseEntity<Medicament> create(@PathVariable long departmentId,
                                             @RequestBody MedicamentDTO dto) {
        final Department department = departmentService.get(departmentId).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(medicamentService.create(dto, department), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'DOCTOR', 'PATIENT')")
    @GetMapping("/medicaments/{id}")
    public ResponseEntity<Medicament> get(@PathVariable Long id) {
        final Medicament medicament = medicamentService.get(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(medicament, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'DOCTOR', 'PATIENT')")
    @GetMapping("/medicaments")
    public ResponseEntity<List<Medicament>> list(@RequestParam(required = false) Long departmentId) {
        return new ResponseEntity<>(medicamentService.list(departmentId), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @PutMapping("/medicaments/{id}")
    public ResponseEntity<Medicament> update(@PathVariable Long id, @RequestBody MedicamentDTO dto) {
        final Medicament medicament = medicamentService.get(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(medicamentService.update(medicament, dto), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @DeleteMapping("/medicaments/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        final Medicament medicament = medicamentService.get(id).orElseThrow(NotFoundException::new);
        medicamentService.delete(medicament);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
