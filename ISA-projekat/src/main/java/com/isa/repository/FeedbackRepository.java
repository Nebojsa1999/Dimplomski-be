package com.isa.repository;

import com.isa.domain.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {


    List<Feedback> findByAppointment_HospitalId(long hospital);
}
