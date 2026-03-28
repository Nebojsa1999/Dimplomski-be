package com.isa.service;

import com.isa.domain.model.Appointment;
import com.isa.domain.model.LabDocument;
import com.isa.repository.LabDocumentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LabDocumentService {

    private final LabDocumentRepository labDocumentRepository;

    @Value("${app.upload.dir:uploads/lab-documents}")
    private String uploadDir;

    @Autowired
    public LabDocumentService(LabDocumentRepository labDocumentRepository) {
        this.labDocumentRepository = labDocumentRepository;
    }

    @Transactional
    public LabDocument upload(Appointment appointment, MultipartFile file) throws IOException {
        final String originalName = file.getOriginalFilename();
        final String extension = originalName != null && originalName.contains(".")
                ? originalName.substring(originalName.lastIndexOf('.') + 1).toLowerCase()
                : "";
        final Path uploadPath = Paths.get(uploadDir);
        Files.createDirectories(uploadPath);
        final Path targetPath = uploadPath.resolve(UUID.randomUUID() + "." + extension);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        final LabDocument document = new LabDocument();
        document.setAppointment(appointment);
        document.setFilePath(targetPath.toString());
        return labDocumentRepository.save(document);
    }

    public Optional<LabDocument> get(Long id) {
        return labDocumentRepository.findById(id);
    }

    public List<LabDocument> listByAppointment(Long appointmentId) {
        return labDocumentRepository.findAllByAppointmentId(appointmentId);
    }

    public void delete(LabDocument document) {
        labDocumentRepository.delete(document);
    }
}
