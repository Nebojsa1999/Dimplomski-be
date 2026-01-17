package com.isa.service;

import com.isa.domain.model.Equipment;
import com.isa.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;

    @Autowired
    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    public Optional<Equipment> get(long id) {
        return equipmentRepository.findById(id);
    }

    public void save(Equipment equipment) {
        equipmentRepository.save(equipment);
    }

    public Equipment create(Equipment equipment) {
        return equipmentRepository.save(equipment);
    }

    public Equipment update(Equipment equipment, Equipment dto) {
        equipment.setName(dto.getName());
        equipment.setAmount(dto.getAmount());
       return equipmentRepository.save(equipment);
    }

    public List<Equipment> getAll(String name) {
        return name != null ? equipmentRepository.findAllByName(name) : equipmentRepository.findAll();
    }

    public List<Equipment> findByHospitalId(long id, String name) {
        return equipmentRepository.findAllByHospitalId(id, name);
    }
}
