package com.isa.domain.api;

import com.isa.domain.dto.DepartmentDTO;
import com.isa.domain.model.Department;
import com.isa.domain.model.Hospital;
import com.isa.exception.NotFoundException;
import com.isa.service.DepartmentService;
import com.isa.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/hospitals")
@PreAuthorize("isAuthenticated()")
public class DepartmentApi {

    private final DepartmentService departmentService;
    private final HospitalService hospitalService;

    @Autowired
    public DepartmentApi(DepartmentService departmentService, HospitalService hospitalService) {
        this.departmentService = departmentService;
        this.hospitalService = hospitalService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @PostMapping("/{hospitalId}/departments")
    public ResponseEntity<Department> create(@PathVariable long hospitalId, @RequestBody DepartmentDTO dto) {
        final Hospital hospital = hospitalService.get(hospitalId).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(departmentService.create(dto, hospital), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @GetMapping("/departments/{id}")
    public ResponseEntity<Department> get(@PathVariable Long id) {
        final Department department = departmentService.get(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(department, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @GetMapping("/departments")
    public ResponseEntity<List<Department>> list(@RequestParam(required = false) Long hospitalId) {
        return new ResponseEntity<>(departmentService.list(hospitalId), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @PutMapping("/departments/{id}")
    public ResponseEntity<Department> update(@PathVariable Long id, @RequestBody DepartmentDTO dto) {
        final Department department = departmentService.get(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(departmentService.update(department, dto), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @DeleteMapping("/departments/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        final Department department = departmentService.get(id).orElseThrow(NotFoundException::new);
        departmentService.delete(department);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
