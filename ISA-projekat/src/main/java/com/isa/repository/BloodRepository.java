package com.isa.repository;

import com.isa.domain.model.BloodSample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BloodRepository extends JpaRepository<BloodSample, Long> {

    List<BloodSample> findBloodByCenterAccountId(long centerAccountId);
}
