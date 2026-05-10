package com.isa.domain.model;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
public class LabDocument extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    private String originalFilename;

    private String contentType;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] content;

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("appointment", appointment)
                .append("originalFilename", originalFilename)
                .append("contentType", contentType)
                .toString();
    }
}
