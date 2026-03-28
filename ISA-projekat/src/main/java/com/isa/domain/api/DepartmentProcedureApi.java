package com.isa.domain.api;

import com.isa.domain.dto.DepartmentProcedureDTO;
import com.isa.domain.model.Department;
import com.isa.domain.model.DepartmentProcedure;
import com.isa.exception.NotFoundException;
import com.isa.service.DepartmentProcedureService;
import com.isa.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/departments")
@PreAuthorize("isAuthenticated()")
public class DepartmentProcedureApi {

    private final DepartmentProcedureService departmentProcedureService;
    private final DepartmentService departmentService;

    @Autowired
    public DepartmentProcedureApi(DepartmentProcedureService departmentProcedureService,
                                  DepartmentService departmentService) {
        this.departmentProcedureService = departmentProcedureService;
        this.departmentService = departmentService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @PostMapping("/{departmentId}/procedures")
    public ResponseEntity<DepartmentProcedure> create(@PathVariable long departmentId,
                                                      @RequestBody DepartmentProcedureDTO dto) {
        final Department department = departmentService.get(departmentId).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(departmentProcedureService.create(dto, department), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @GetMapping("/procedures/{id}")
    public ResponseEntity<DepartmentProcedure> get(@PathVariable Long id) {
        final DepartmentProcedure procedure = departmentProcedureService.get(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(procedure, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @GetMapping("/procedures")
    public ResponseEntity<List<DepartmentProcedure>> list(
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) String name) {
        return new ResponseEntity<>(departmentProcedureService.list(departmentId, name), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @PutMapping("/procedures/{id}")
    public ResponseEntity<DepartmentProcedure> update(@PathVariable Long id,
                                                      @RequestBody DepartmentProcedureDTO dto) {
        final DepartmentProcedure procedure = departmentProcedureService.get(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(departmentProcedureService.update(procedure, dto), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM')")
    @DeleteMapping("/procedures/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        final DepartmentProcedure procedure = departmentProcedureService.get(id).orElseThrow(NotFoundException::new);
        departmentProcedureService.delete(procedure);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
