package com.isa.service;

import com.isa.domain.dto.DepartmentProcedureDTO;
import com.isa.domain.model.Department;
import com.isa.domain.model.DepartmentProcedure;
import com.isa.repository.DepartmentProcedureRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentProcedureService {

    private final DepartmentProcedureRepository departmentProcedureRepository;

    @Autowired
    public DepartmentProcedureService(DepartmentProcedureRepository departmentProcedureRepository) {
        this.departmentProcedureRepository = departmentProcedureRepository;
    }

    @Transactional
    public DepartmentProcedure create(DepartmentProcedureDTO dto, Department department) {
        final DepartmentProcedure procedure = new DepartmentProcedure();
        procedure.setName(dto.getName());
        procedure.setDescription(dto.getDescription());
        procedure.setPrice(dto.getPrice());
        procedure.setDepartment(department);
        return departmentProcedureRepository.save(procedure);
    }

    public Optional<DepartmentProcedure> get(Long id) {
        return departmentProcedureRepository.findById(id);
    }

    public List<DepartmentProcedure> list(Long departmentId, String name) {
        return departmentProcedureRepository.findAllFiltered(departmentId, name);
    }

    @Transactional
    public DepartmentProcedure update(DepartmentProcedure procedure, DepartmentProcedureDTO dto) {
        procedure.setName(dto.getName());
        procedure.setDescription(dto.getDescription());
        procedure.setPrice(dto.getPrice());
        return departmentProcedureRepository.save(procedure);
    }

    public void delete(DepartmentProcedure procedure) {
        departmentProcedureRepository.delete(procedure);
    }
}
