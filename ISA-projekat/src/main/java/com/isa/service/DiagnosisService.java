package com.isa.service;

import com.isa.domain.dto.DiagnosisDTO;
import com.isa.domain.model.Department;
import com.isa.domain.model.Diagnosis;
import com.isa.repository.DiagnosisRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DiagnosisService {

    private final DiagnosisRepository diagnosisRepository;

    @Autowired
    public DiagnosisService(DiagnosisRepository diagnosisRepository) {
        this.diagnosisRepository = diagnosisRepository;
    }

    @Transactional
    public Diagnosis create(DiagnosisDTO dto, Department department) {
        diagnosisRepository.findByCodeAndDepartmentId(dto.getCode(), department.getId())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException(
                            "A diagnosis with code '" + dto.getCode() + "' already exists in this department.");
                });
        final Diagnosis diagnosis = new Diagnosis();
        mapDtoToEntity(dto, diagnosis);
        diagnosis.setDepartment(department);
        return diagnosisRepository.save(diagnosis);
    }

    public Optional<Diagnosis> get(Long id) {
        return diagnosisRepository.findById(id);
    }

    public List<Diagnosis> list(Long departmentId) {
        return departmentId != null
                ? diagnosisRepository.findAllByDepartmentId(departmentId)
                : diagnosisRepository.findAll();
    }

    @Transactional
    public Diagnosis update(Diagnosis diagnosis, DiagnosisDTO dto) {
        diagnosisRepository.findByCodeAndDepartmentId(dto.getCode(), diagnosis.getDepartment().getId())
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

    private void mapDtoToEntity(DiagnosisDTO dto, Diagnosis diagnosis) {
        diagnosis.setCode(dto.getCode());
        diagnosis.setName(dto.getName());
        diagnosis.setDescription(dto.getDescription());
    }
}
