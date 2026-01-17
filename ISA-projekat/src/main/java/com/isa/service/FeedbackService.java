package com.isa.service;

import com.isa.domain.dto.FeedbackDto;
import com.isa.domain.model.Appointment;
import com.isa.domain.model.Hospital;
import com.isa.domain.model.Feedback;
import com.isa.exception.NotFoundException;
import com.isa.repository.FeedbackRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserService userService;
    private final AppointmentService appointmentService;

    private final HospitalService hospitalService;
    @Autowired
    public FeedbackService(FeedbackRepository feedbackRepository, UserService userService, AppointmentService appointmentService, HospitalService hospitalService) {
        this.feedbackRepository = feedbackRepository;
        this.userService = userService;
        this.appointmentService = appointmentService;
        this.hospitalService = hospitalService;
    }

    public List<Feedback> findAllByAppointment(Appointment appointment) {
        return feedbackRepository.findByAppointment_HospitalId(appointment.getHospital().getId());
    }

    @Transactional
    public Feedback create(FeedbackDto feedbackDto) {
        final Feedback feedback = new Feedback();
        feedback.setComment(feedbackDto.getComment());
        feedback.setGrade(Double.parseDouble(feedbackDto.getGrade()));
        feedback.setPatient(userService.get(feedbackDto.getPatientId()).orElseThrow(NotFoundException::new));
        feedback.setAppointment(appointmentService.get(feedbackDto.getAppointmentId()).orElseThrow(NotFoundException::new));
        feedbackRepository.save(feedback);
        hospitalService.getAverageRating(feedback.getAppointment());
        final Hospital hospital = hospitalService.get(feedback.getAppointment().getHospital().getId()).orElseThrow(NotFoundException::new);
        hospitalService.save(hospital);

        return feedback;
    }
}
