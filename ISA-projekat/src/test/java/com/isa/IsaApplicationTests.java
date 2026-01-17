package com.isa;

import com.isa.exception.NotFoundException;
import com.isa.service.AppointmentService;
import com.isa.service.HospitalService;
import com.isa.service.FeedbackService;
import com.isa.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class IsaApplicationTests {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private UserService userService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private HospitalService hospitalService;

    @Test
    void contextLoads() {
    }

    @Test
    public void averageTest() {
        Double averageRating = hospitalService.getAverageRating(appointmentService.get(1).orElseThrow(NotFoundException::new));
        Assertions.assertEquals(8, averageRating);
    }

}
