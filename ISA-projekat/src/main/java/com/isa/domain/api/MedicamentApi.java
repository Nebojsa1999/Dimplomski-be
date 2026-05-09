package com.isa.domain.api;

import com.isa.domain.dto.MedicamentDTO;
import com.isa.domain.model.Medicament;
import com.isa.exception.NotFoundException;
import com.isa.service.MedicamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/medicaments")
@PreAuthorize("isAuthenticated()")
public class MedicamentApi {

    private final MedicamentService medicamentService;

    @Autowired
    public MedicamentApi(MedicamentService medicamentService) {
        this.medicamentService = medicamentService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @PostMapping
    public ResponseEntity<Medicament> create(@RequestBody MedicamentDTO dto) {
        return new ResponseEntity<>(medicamentService.create(dto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'DOCTOR', 'PATIENT')")
    @GetMapping("/{id}")
    public ResponseEntity<Medicament> get(@PathVariable Long id) {
        final Medicament medicament = medicamentService.get(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(medicament, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'DOCTOR', 'PATIENT')")
    @GetMapping
    public ResponseEntity<List<Medicament>> list(@RequestParam(required = false) String departmentName, @RequestParam(required = false) String name) {
        return new ResponseEntity<>(medicamentService.list(departmentName, name), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @PutMapping("/{id}")
    public ResponseEntity<Medicament> update(@PathVariable Long id, @RequestBody MedicamentDTO dto) {
        final Medicament medicament = medicamentService.get(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(medicamentService.update(medicament, dto), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        final Medicament medicament = medicamentService.get(id).orElseThrow(NotFoundException::new);
        medicamentService.delete(medicament);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
