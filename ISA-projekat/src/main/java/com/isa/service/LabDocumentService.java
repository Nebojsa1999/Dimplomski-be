package com.isa.service;

import com.isa.domain.model.Appointment;
import com.isa.domain.model.LabDocument;
import com.isa.repository.LabDocumentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class LabDocumentService {

    private final LabDocumentRepository labDocumentRepository;

    @Autowired
    public LabDocumentService(LabDocumentRepository labDocumentRepository) {
        this.labDocumentRepository = labDocumentRepository;
    }

    @Transactional
    public LabDocument upload(Appointment appointment, MultipartFile file) throws IOException {
        final LabDocument document = new LabDocument();
        document.setAppointment(appointment);
        document.setOriginalFilename(file.getOriginalFilename());
        document.setContentType(file.getContentType());
        document.setContent(file.getBytes());
        return labDocumentRepository.save(document);
    }

    public Optional<LabDocument> get(Long id) {
        return labDocumentRepository.findById(id);
    }

    public LabDocument listByAppointment(Long appointmentId) {
        return labDocumentRepository.findByAppointmentId(appointmentId);
    }

    public void delete(LabDocument document) {
        labDocumentRepository.delete(document);
    }
}
