package com.isa.service;


import com.isa.domain.dto.HospitalDto;
import com.isa.domain.model.Hospital;
import com.isa.domain.model.Feedback;
import com.isa.repository.HospitalRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class HospitalService {

    private final HospitalRepository hospitalRepository;

    private final FeedbackService feedbackService;

    @Autowired
    public HospitalService(HospitalRepository hospitalRepository, @Lazy FeedbackService feedbackService) {
        this.hospitalRepository = hospitalRepository;
        this.feedbackService = feedbackService;
    }

    public Hospital update(Hospital hospital, HospitalDto hospitalDto) {
        hospital.setName(hospitalDto.getName());
        hospital.setAddress(hospitalDto.getAddress());
        hospital.setCountry(hospitalDto.getCountry());
        hospital.setCity(hospitalDto.getCity());
        final double longitude = hospitalDto.getLongitude() == null ? 0 : Double.parseDouble(hospitalDto.getLongitude());
        final double latitude = hospitalDto.getLatitude() == null ? 0 : Double.parseDouble(hospitalDto.getLatitude());
        hospital.setLatitude(latitude);
        hospital.setLongitude(longitude);
        hospital.setDescription(hospitalDto.getDescription());
        hospital.setStartTime(LocalTime.parse(hospitalDto.getStartTime()));
        hospital.setEndTime(LocalTime.parse(hospitalDto.getEndTime()));
        hospitalRepository.save(hospital);
        return hospital;
    }

    public Hospital create(HospitalDto hospitalDto) {
        final Hospital hospital = new Hospital();
        hospital.setName(hospitalDto.getName());
        hospital.setAddress(hospitalDto.getAddress());
        hospital.setCountry(hospitalDto.getCountry());
        hospital.setCity(hospitalDto.getCity());
        final double longitude = hospitalDto.getLongitude() == null ? 0 : Double.parseDouble(hospitalDto.getLongitude());
        final double latitude = hospitalDto.getLatitude() == null ? 0 : Double.parseDouble(hospitalDto.getLatitude());
        hospital.setLatitude(latitude);
        hospital.setLongitude(longitude);
        hospital.setDescription(hospitalDto.getDescription());
        hospital.setStartTime(LocalTime.parse(hospitalDto.getStartTime()));
        hospital.setEndTime(LocalTime.parse(hospitalDto.getEndTime()));
        return hospitalRepository.save(hospital);
    }

    public Double getAverageRating(Hospital hospital) {
        final List<Feedback> allByHospital = feedbackService.findAllByHospital(hospital);
        return allByHospital.stream()
                .mapToDouble(Feedback::getGrade)
                .average()
                .orElse(0D);
    }

    public Optional<Hospital> get(long id) {
        return hospitalRepository.findById(id);
    }

    public List<Hospital> list(String name) {
        return StringUtils.isNotEmpty(name) ? hospitalRepository.findByName(name) : hospitalRepository.findAll();
    }

    public void save(Hospital hospital) {
        hospitalRepository.save(hospital);
    }
}
