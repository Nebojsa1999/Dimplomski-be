package com.isa.service;

import com.isa.domain.dto.MedicamentDTO;
import com.isa.domain.model.Department;
import com.isa.domain.model.Medicament;
import com.isa.repository.MedicamentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicamentService {

    private final MedicamentRepository medicamentRepository;

    @Autowired
    public MedicamentService(MedicamentRepository medicamentRepository) {
        this.medicamentRepository = medicamentRepository;
    }

    @Transactional
    public Medicament create(MedicamentDTO dto, Department department) {
        final Medicament medicament = new Medicament();
        mapDtoToEntity(dto, medicament);
        medicament.setDepartment(department);
        return medicamentRepository.save(medicament);
    }

    public Optional<Medicament> get(Long id) {
        return medicamentRepository.findById(id);
    }

    public List<Medicament> list(Long departmentId) {
        return departmentId != null
                ? medicamentRepository.findAllByDepartmentId(departmentId)
                : medicamentRepository.findAll();
    }

    @Transactional
    public Medicament update(Medicament medicament, MedicamentDTO dto) {
        mapDtoToEntity(dto, medicament);
        return medicamentRepository.save(medicament);
    }

    public void delete(Medicament medicament) {
        medicamentRepository.delete(medicament);
    }

    private void mapDtoToEntity(MedicamentDTO dto, Medicament medicament) {
        medicament.setName(dto.getName());
        medicament.setDosage(dto.getDosage());
        medicament.setInstructions(dto.getInstructions());
    }
}
