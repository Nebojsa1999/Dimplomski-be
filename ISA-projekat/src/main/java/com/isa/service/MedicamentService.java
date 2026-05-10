package com.isa.service;

import com.isa.domain.dto.MedicamentDTO;
import com.isa.domain.model.DepartmentName;
import com.isa.domain.model.Medicament;
import com.isa.repository.DepartmentNameRepository;
import com.isa.repository.MedicamentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicamentService {

    private final MedicamentRepository medicamentRepository;
    private final DepartmentNameRepository departmentNameRepository;

    @Autowired
    public MedicamentService(MedicamentRepository medicamentRepository,
                             DepartmentNameRepository departmentNameRepository) {
        this.medicamentRepository = medicamentRepository;
        this.departmentNameRepository = departmentNameRepository;
    }

    @Transactional
    public Medicament create(MedicamentDTO dto) {
        final DepartmentName deptName = findOrCreateDepartmentName(dto.getDepartmentName());
        final Medicament medicament = new Medicament();
        mapDtoToEntity(dto, medicament);
        medicament.setDepartmentName(deptName);
        return medicamentRepository.save(medicament);
    }

    public Optional<Medicament> get(Long id) {
        return medicamentRepository.findById(id);
    }

    public List<Medicament> list(String departmentName, String name) {
        if (name != null && !name.isBlank()) {
            return medicamentRepository.findAllFiltered(departmentName, name);
        }
        return departmentName != null
                ? medicamentRepository.findAllByDepartmentNameName(departmentName)
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

    private DepartmentName findOrCreateDepartmentName(String name) {
        return departmentNameRepository.findByName(name)
                .orElseGet(() -> {
                    final DepartmentName newDeptName = new DepartmentName();
                    newDeptName.setName(name);
                    return departmentNameRepository.save(newDeptName);
                });
    }

    private void mapDtoToEntity(MedicamentDTO dto, Medicament medicament) {
        medicament.setName(dto.getName());
        medicament.setDosage(dto.getDosage());
        medicament.setInstructions(dto.getInstructions());
    }
}
