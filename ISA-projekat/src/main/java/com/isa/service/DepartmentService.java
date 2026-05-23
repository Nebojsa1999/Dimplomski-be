package com.isa.service;

import com.isa.domain.dto.DepartmentDTO;
import com.isa.domain.model.Department;
import com.isa.domain.model.DepartmentName;
import com.isa.domain.model.Hospital;
import com.isa.repository.DepartmentNameRepository;
import com.isa.repository.DepartmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentNameRepository departmentNameRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository,
                             DepartmentNameRepository departmentNameRepository) {
        this.departmentRepository = departmentRepository;
        this.departmentNameRepository = departmentNameRepository;
    }

    @Transactional
    public Department create(DepartmentDTO dto, Hospital hospital) {
        departmentNameRepository.findByName(dto.getName()).orElseGet(() -> {
            final DepartmentName departmentName = new DepartmentName();
            departmentName.setName(dto.getName());
            return departmentNameRepository.save(departmentName);
        });
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

    public List<Department> list(Long hospitalId, String name) {
        if (name != null && !name.isBlank()) {
            return departmentRepository.findAllByHospitalIdAndName(hospitalId, name);
        }
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

    @Transactional
    public void delete(Department department) {
        final String name = department.getName();
        departmentRepository.delete(department);
        if (departmentRepository.countByName(name) == 0) {
            departmentNameRepository.findByName(name).ifPresent(departmentNameRepository::delete);
        }
    }

    public List<DepartmentName> listNames() {
        return departmentNameRepository.findAll();
    }

    public Optional<DepartmentName> getName(Long id) {
        return departmentNameRepository.findById(id);
    }
}
