package com.isa.service;

import com.isa.domain.dto.FeedbackDto;
import com.isa.domain.model.Appointment;
import com.isa.domain.model.Feedback;
import com.isa.domain.model.User;
import com.isa.enums.AppointmentStatus;
import com.isa.repository.FeedbackRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public double getAverageRatingForDoctor(User doctor) {
        return feedbackRepository.findAverageGradeByDoctorId(doctor.getId()).orElse(0.0);
    }

    @Transactional
    public Feedback create(Appointment appointment, FeedbackDto feedbackDto) {
        if (appointment.getAppointmentStatus() != AppointmentStatus.FINISHED) {
            throw new IllegalArgumentException("Feedback can only be submitted for finished appointments.");
        }
        if (feedbackDto.getGrade() < 1 || feedbackDto.getGrade() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        if (feedbackRepository.findByAppointmentId(appointment.getId()).isPresent()) {
            throw new IllegalArgumentException("Feedback already submitted for this appointment.");
        }

        final Feedback feedback = new Feedback();
        feedback.setComment(feedbackDto.getComment());
        feedback.setGrade(feedbackDto.getGrade());
        feedback.setAppointment(appointment);
        return feedbackRepository.save(feedback);
    }
}
