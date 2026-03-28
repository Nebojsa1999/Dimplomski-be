package com.isa.service;

import com.isa.domain.dto.DepartmentDTO;
import com.isa.domain.model.Department;
import com.isa.domain.model.Hospital;
import com.isa.repository.DepartmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Transactional
    public Department create(DepartmentDTO dto, Hospital hospital) {
        final Department department = new Department();
        department.setName(dto.getName());
        department.setDescription(dto.getDescription());
        department.setPhoneNumber(dto.getPhoneNumber());
        department.setHospital(hospital);
        return departmentRepository.save(department);
    }

    public Optional<Department> get(Long id) {
        return departmentRepository.findById(id);
    }

    public List<Department> list(Long hospitalId) {
        return hospitalId != null
                ? departmentRepository.findAllByHospitalId(hospitalId)
                : departmentRepository.findAll();
    }

    @Transactional
    public Department update(Department department, DepartmentDTO dto) {
        department.setName(dto.getName());
        department.setDescription(dto.getDescription());
        department.setPhoneNumber(dto.getPhoneNumber());
        return departmentRepository.save(department);
    }

    public void delete(Department department) {
        departmentRepository.delete(department);
    }
}
