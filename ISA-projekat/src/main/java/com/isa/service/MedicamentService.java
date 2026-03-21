package com.isa.service;

import com.isa.domain.model.Medicament;
import com.isa.repository.MedicamentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MedicamentService {

    private final MedicamentRepository medicamentRepository;

    @Autowired
    public MedicamentService(MedicamentRepository medicamentRepository) {
        this.medicamentRepository = medicamentRepository;
    }

    @Transactional
    public Medicament create(Medicament dto) {
        final Medicament medicament = new Medicament();
        medicament.setName(dto.getName());
        medicament.setDosage(dto.getDosage());
        medicament.setInstructions(dto.getInstructions());
        medicament.setDepartment(dto.getDepartment());

        return medicamentRepository.save(medicament);
    }
}
