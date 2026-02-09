package com.isa.service;

import com.isa.domain.dto.FeedbackDto;
import com.isa.domain.model.Appointment;
import com.isa.domain.model.Feedback;
import com.isa.domain.model.Hospital;
import com.isa.repository.FeedbackRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    @Autowired
    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public Optional<Feedback> findByAppointment(Appointment appointment) {
        return feedbackRepository.findByAppointmentId(appointment.getId());
    }

    public List<Feedback> findAllByHospital(Hospital hospital) {
        return feedbackRepository.findAllByAppointment_Hospital(hospital);
    }

    @Transactional
    public Feedback create(Appointment appointment, FeedbackDto feedbackDto) {
        final Feedback feedback = new Feedback();
        feedback.setComment(feedbackDto.getComment());
        feedback.setGrade(feedbackDto.getGrade());
        feedback.setPatient(appointment.getPatient());
        feedback.setAppointment(appointment);
        feedbackRepository.save(feedback);

        return feedback;
    }
}
