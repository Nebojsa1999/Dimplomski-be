package com.isa.service;

import com.isa.domain.dto.DiagnosisDTO;
import com.isa.domain.model.DepartmentName;
import com.isa.domain.model.Diagnosis;
import com.isa.repository.DepartmentNameRepository;
import com.isa.repository.DiagnosisRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiagnosisService {

    private final DiagnosisRepository diagnosisRepository;
    private final DepartmentNameRepository departmentNameRepository;

    @Autowired
    public DiagnosisService(DiagnosisRepository diagnosisRepository,
                            DepartmentNameRepository departmentNameRepository) {
        this.diagnosisRepository = diagnosisRepository;
        this.departmentNameRepository = departmentNameRepository;
    }

    @Transactional
    public Diagnosis create(DiagnosisDTO dto) {
        final String departmentName = dto.getDepartmentName();
        final DepartmentName deptName = findOrCreateDepartmentName(departmentName);
        diagnosisRepository.findByCodeAndDepartmentNameName(dto.getCode(), departmentName)
                .ifPresent(existing -> {
                    throw new IllegalArgumentException(
                            "A diagnosis with code '" + dto.getCode() + "' already exists in this department.");
                });
        final Diagnosis diagnosis = new Diagnosis();
        mapDtoToEntity(dto, diagnosis);
        diagnosis.setDepartmentName(deptName);
        return diagnosisRepository.save(diagnosis);
    }

    public Optional<Diagnosis> get(Long id) {
        return diagnosisRepository.findById(id);
    }

    public List<Diagnosis> list(String departmentName) {
        return departmentName != null
                ? diagnosisRepository.findAllByDepartmentNameName(departmentName)
                : diagnosisRepository.findAll();
    }

    @Transactional
    public Diagnosis update(Diagnosis diagnosis, DiagnosisDTO dto) {
        diagnosisRepository.findByCodeAndDepartmentNameName(dto.getCode(), diagnosis.getDepartmentName().getName())
                .filter(existing -> !existing.getId().equals(diagnosis.getId()))
                .ifPresent(existing -> {
                    throw new IllegalArgumentException(
                            "A diagnosis with code '" + dto.getCode() + "' already exists in this department.");
                });
        mapDtoToEntity(dto, diagnosis);
        return diagnosisRepository.save(diagnosis);
    }

    public void delete(Diagnosis diagnosis) {
        diagnosisRepository.delete(diagnosis);
    }

    private DepartmentName findOrCreateDepartmentName(String name) {
        return departmentNameRepository.findByName(name).orElseGet(() -> {
                    final DepartmentName newDeptName = new DepartmentName();
                    newDeptName.setName(name);
                    return departmentNameRepository.save(newDeptName);
                });
    }

    private void mapDtoToEntity(DiagnosisDTO dto, Diagnosis diagnosis) {
        diagnosis.setCode(dto.getCode());
        diagnosis.setName(dto.getName());
        diagnosis.setDescription(dto.getDescription());
    }
}
