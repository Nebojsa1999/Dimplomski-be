package com.isa.service;

import com.isa.domain.model.Department;
import com.isa.repository.DepartmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Transactional
    public Department create(Department dto) {
        final Department department = new Department();
        department.setName(dto.getName());
        department.setDescription(dto.getDescription());
        department.setPhoneNumber(dto.getPhoneNumber());
        department.setHospital(dto.getHospital());

        return departmentRepository.save(department);

    }
}
