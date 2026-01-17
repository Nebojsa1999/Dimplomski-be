package com.isa.service;

import com.isa.domain.dto.AppointmentReportDto;
import com.isa.domain.model.AppointmentReport;
import com.isa.domain.model.Equipment;
import com.isa.domain.model.User;
import com.isa.enums.BloodType;
import com.isa.exception.NotFoundException;
import com.isa.repository.AppointmentReportRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentReportService {

    private final AppointmentReportRepository appointmentReportRepository;

    private final EquipmentService equipmentService;


    @Autowired
    public AppointmentReportService(AppointmentReportRepository appointmentReportRepository, UserService userService, EquipmentService equipmentService) {
        this.appointmentReportRepository = appointmentReportRepository;
        this.equipmentService = equipmentService;
    }

    @Transactional
    public AppointmentReport create(AppointmentReportDto appointmentReportDto, User user) {
        final AppointmentReport appointmentReport = new AppointmentReport();
        appointmentReport.setBagType(appointmentReportDto.getBagType());
        appointmentReport.setBloodType(BloodType.valueOf(appointmentReportDto.getBloodType()));
        appointmentReport.setBloodAmount(Double.parseDouble(appointmentReportDto.getBloodAmount()));
        appointmentReport.setCopperSulfate(appointmentReportDto.getCopperSulfate());
        final Equipment equipment = equipmentService.get(appointmentReportDto.getEquipmentId()).orElseThrow(NotFoundException::new);
        appointmentReport.setEquipment(equipment);
        appointmentReport.setEndOfGiving(appointmentReportDto.getEndOfGiving());
        appointmentReport.setHeart(appointmentReportDto.getHeart());
        appointmentReport.setHemoglobinometer(appointmentReportDto.getHemoglobinometer());
        appointmentReport.setLungs(appointmentReportDto.getLungs());
        appointmentReport.setNote(appointmentReportDto.getNote());
        appointmentReport.setNoteToDoctor(appointmentReportDto.getNoteToDoctor());
        appointmentReport.setPunctureSite(appointmentReportDto.getPunctureSite());
        appointmentReport.setReasonForPrematureTerminationOfBloodDonation(appointmentReportDto.getReasonForPrematureTerminationOfBloodDonation());
        appointmentReport.setStartOfGiving(appointmentReportDto.getStartOfGiving());
        appointmentReport.setTA(appointmentReportDto.getTA());
        appointmentReport.setTT(appointmentReportDto.getTt());
        appointmentReport.setTV(appointmentReportDto.getTv());
        appointmentReport.setDenied(Boolean.parseBoolean(appointmentReportDto.getDenied()));
        appointmentReport.setReasonForDenying(appointmentReportDto.getReasonForDenying());
        appointmentReport.setEquipmentAmount(Double.parseDouble(appointmentReportDto.getEquipmentAmount()));

        equipment.setAmount(equipment.getAmount() - Double.parseDouble(appointmentReportDto.getEquipmentAmount()));
        equipmentService.save(equipment);

        appointmentReportRepository.save(appointmentReport);
        return appointmentReport;
    }
}
