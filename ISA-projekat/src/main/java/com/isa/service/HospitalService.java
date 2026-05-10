package com.isa.service;

import com.isa.domain.dto.HospitalDto;
import com.isa.domain.model.Hospital;
import com.isa.repository.FeedbackRepository;
import com.isa.repository.HospitalRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final FeedbackRepository feedbackRepository;

    @Autowired
    public HospitalService(HospitalRepository hospitalRepository, FeedbackRepository feedbackRepository) {
        this.hospitalRepository = hospitalRepository;
        this.feedbackRepository = feedbackRepository;
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
        return hospitalRepository.save(hospital);
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

    public double getAverageRating(Hospital hospital) {
        return feedbackRepository.findAverageGradeByHospitalId(hospital.getId()).orElse(0.0);
    }

    public void recalculateRating(Hospital hospital) {
        final double avg = getAverageRating(hospital);
        hospital.setAverageRating(avg);
        hospitalRepository.save(hospital);
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

    public void delete(Hospital hospital) {
        hospitalRepository.delete(hospital);
    }
}
