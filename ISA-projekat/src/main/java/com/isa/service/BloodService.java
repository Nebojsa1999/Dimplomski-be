package com.isa.service;

import com.isa.domain.model.BloodSample;
import com.isa.domain.model.CenterAccount;
import com.isa.repository.BloodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BloodService {

    private final BloodRepository bloodRepository;

    @Autowired
    public BloodService(BloodRepository bloodRepository) {
        this.bloodRepository = bloodRepository;
    }

    public List<BloodSample> getBloodSampleByCenterAccount(CenterAccount centerAccount) {
        return bloodRepository.findBloodByCenterAccountId(centerAccount.getId());
    }

    public BloodSample create(BloodSample bloodSample) {
        final BloodSample newBloodSample = new BloodSample();
        newBloodSample.setAmount(bloodSample.getAmount());
        newBloodSample.setBloodType(bloodSample.getBloodType());
        newBloodSample.setCenterAccount(bloodSample.getCenterAccount());
        bloodRepository.save(newBloodSample);
        return newBloodSample;
    }
}
