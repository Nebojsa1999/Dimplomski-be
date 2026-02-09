package com.isa.repository;

import com.isa.domain.model.Feedback;
import com.isa.domain.model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {


    Optional<Feedback> findByAppointmentId(long appointmentId);

    List<Feedback> findAllByAppointment_Hospital(Hospital appointmentHospital);
}
