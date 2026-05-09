package com.isa.service;

import com.isa.domain.model.Appointment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailService.class);
    private static final String BREVO_API_URL = "https://api.brevo.com/v3/smtp/email";
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm").withZone(ZoneId.of("Europe/Belgrade"));

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${app.base-url}")
    private String baseUrl;

    private String baseUrl() {
        return baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    @Value("${brevo.api-key}")
    private String apiKey;

    @Value("${brevo.from}")
    private String fromAddress;

    @Async
    public void sendVerificationEmail(String toAddress, String token) {
        final String body = """
                {
                  "sender": { "email": "%s" },
                  "to": [{ "email": "%s" }],
                  "subject": "Hospital System – Email Verification",
                  "textContent": "Thank you for registering.\\n\\nYour registered email: %s\\n\\nPlease verify your email address by clicking the link below:\\n\\n%s/api/verify?token=%s\\n\\nThis link expires in 24 hours.\\n\\nIf you did not register, please ignore this email."
                }
                """.formatted(fromAddress, toAddress, toAddress, baseUrl(), token);
        send(toAddress, body);
    }

    @Async
    public void sendAppointmentScheduledEmail(String toAddress, Appointment appointment) {
        final String dateStr = FORMATTER.format(appointment.getDateAndTime());
        final String doctorName = appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName();
        final String body = """
                {
                  "sender": { "email": "%s" },
                  "to": [{ "email": "%s" }],
                  "subject": "Appointment Scheduled",
                  "textContent": "Your appointment has been scheduled successfully.\\n\\nDoctor: %s\\nDate & Time: %s\\nDuration: %d minutes\\n\\nPlease arrive on time. To cancel, log in to the system."
                }
                """.formatted(fromAddress, toAddress, doctorName, dateStr, appointment.getDuration());
        send(toAddress, body);
    }

    @Async
    public void sendAppointmentCancelledEmail(String toAddress, Appointment appointment) {
        final String dateStr = FORMATTER.format(appointment.getDateAndTime());
        final String doctorName = appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName();
        final String body = """
                {
                  "sender": { "email": "%s" },
                  "to": [{ "email": "%s" }],
                  "subject": "Appointment Cancelled",
                  "textContent": "Your appointment has been cancelled.\\n\\nDoctor: %s\\nDate & Time: %s\\n\\nIf you have questions, please contact the hospital."
                }
                """.formatted(fromAddress, toAddress, doctorName, dateStr);
        send(toAddress, body);
    }

    private void send(String toAddress, String body) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", apiKey);
        try {
            restTemplate.postForEntity(BREVO_API_URL, new HttpEntity<>(body, headers), String.class);
        } catch (Exception e) {
            LOG.error("Failed to send email to {}: {}", toAddress, e.getMessage(), e);
        }
    }
}
