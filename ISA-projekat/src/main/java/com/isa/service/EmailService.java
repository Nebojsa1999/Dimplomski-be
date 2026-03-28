package com.isa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.base-url:http://localhost:8081}")
    private String baseUrl;

    @Value("${spring.mail.username}")
    private String fromAddress;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String token) {
        final SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(fromAddress);
        message.setSubject("Hospital System – Email Verification");
        message.setText(
                "Thank you for registering.\n\n"
                + "Please verify your email address by clicking the link below:\n\n"
                + baseUrl + "/api/verify?token=" + token
                + "\n\nThis link expires in 24 hours.\n\n"
                + "If you did not register, please ignore this email."
        );
        mailSender.send(message);
    }
}
