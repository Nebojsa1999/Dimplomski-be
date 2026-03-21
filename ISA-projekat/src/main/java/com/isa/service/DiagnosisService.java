package com.isa.service;

import com.isa.domain.model.Diagnosis;
import com.isa.repository.DiagnosisRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DiagnosisService {

    private final DiagnosisRepository diagnosisRepository;

    @Autowired
    public DiagnosisService(DiagnosisRepository diagnosisRepository) {
        this.diagnosisRepository = diagnosisRepository;
    }

    @Transactional
    public Diagnosis create(Diagnosis dto) {
        final Diagnosis diagnosis = new Diagnosis();
        diagnosis.setCode(dto.getCode());
        diagnosis.setName(dto.getName());
        diagnosis.setDescription(dto.getDescription());
        diagnosis.setDepartment(dto.getDepartment());

        return diagnosisRepository.save(diagnosis);
    }
}
