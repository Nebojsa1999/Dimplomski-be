package com.isa.domain.api;

import com.isa.domain.model.DepartmentName;
import com.isa.exception.NotFoundException;
import com.isa.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/department-names")
@PreAuthorize("isAuthenticated()")
public class DepartmentNameApi {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentNameApi(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'PATIENT')")
    @GetMapping
    public ResponseEntity<List<DepartmentName>> list() {
        return new ResponseEntity<>(departmentService.listNames(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'PATIENT')")
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentName> get(@PathVariable Long id) {
        return departmentService.getName(id)
                .map(dn -> new ResponseEntity<>(dn, HttpStatus.OK))
                .orElseThrow(NotFoundException::new);
    }
}
