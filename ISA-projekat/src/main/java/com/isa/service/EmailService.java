package com.isa.service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailService.class);

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${resend.api-key}")
    private String resendApiKey;

    @Value("${resend.from}")
    private String fromAddress;

    @Async
    public void sendVerificationEmail(String toAddress, String token) {
        final Resend resend = new Resend(resendApiKey);
        final CreateEmailOptions email = CreateEmailOptions.builder()
                .from(fromAddress)
                .to(toAddress)
                .subject("Hospital System – Email Verification")
                .text(
                        "Thank you for registering.\n\n"
                        + "Your registered email: " + toAddress + "\n\n"
                        + "Please verify your email address by clicking the link below:\n\n"
                        + baseUrl + "/api/verify?token=" + token
                        + "\n\nThis link expires in 24 hours.\n\n"
                        + "If you did not register, please ignore this email."
                )
                .build();
        try {
            resend.emails().send(email);
        } catch (ResendException e) {
            LOG.error("Failed to send verification email to {}: {}", toAddress, e.getMessage(), e);
        }
    }
}
