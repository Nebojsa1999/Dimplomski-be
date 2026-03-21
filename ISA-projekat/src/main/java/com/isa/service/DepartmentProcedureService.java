package com.isa.service;

import com.isa.domain.model.DepartmentProcedure;
import com.isa.repository.DepartmentProcedureRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentProcedureService {

    private final DepartmentProcedureRepository departmentProcedureRepository;

    @Autowired
    public DepartmentProcedureService(DepartmentProcedureRepository departmentProcedureRepository) {
        this.departmentProcedureRepository = departmentProcedureRepository;
    }

    @Transactional
    public DepartmentProcedure create(DepartmentProcedure dto) {
        final DepartmentProcedure departmentProcedure = new DepartmentProcedure();
        departmentProcedure.setName(dto.getName());
        departmentProcedure.setDescription(dto.getDescription());
        departmentProcedure.setPrice(dto.getPrice());
        departmentProcedure.setDepartment(dto.getDepartment());

        return departmentProcedureRepository.save(departmentProcedure);
    }
}
