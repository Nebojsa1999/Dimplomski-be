package com.isa.domain.api;

import com.isa.domain.model.Appointment;
import com.isa.domain.model.LabDocument;
import com.isa.exception.NotFoundException;
import com.isa.service.AppointmentService;
import com.isa.service.LabDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/appointments")
@PreAuthorize("isAuthenticated()")
public class LabDocumentApi {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("pdf", "jpg", "jpeg", "png");

    private final LabDocumentService labDocumentService;
    private final AppointmentService appointmentService;

    @Autowired
    public LabDocumentApi(LabDocumentService labDocumentService, AppointmentService appointmentService) {
        this.labDocumentService = labDocumentService;
        this.appointmentService = appointmentService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'DOCTOR')")
    @PostMapping("/{appointmentId}/lab-documents")
    public ResponseEntity<LabDocument> upload(@PathVariable long appointmentId,
                                              @RequestParam("file") MultipartFile file) throws IOException {
        final String originalName = file.getOriginalFilename();
        final String extension = originalName != null && originalName.contains(".")
                ? originalName.substring(originalName.lastIndexOf('.') + 1).toLowerCase()
                : "";
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("Unsupported file type. Allowed types: PDF, JPG, PNG.");
        }
        final Appointment appointment = appointmentService.get(appointmentId).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(labDocumentService.upload(appointment, file), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'DOCTOR', 'PATIENT')")
    @GetMapping("/{appointmentId}/lab-documents")
    public ResponseEntity<List<LabDocument>> listByAppointment(@PathVariable long appointmentId) {
        final Appointment appointment = appointmentService.get(appointmentId).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(labDocumentService.listByAppointment(appointment.getId()), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'DOCTOR', 'PATIENT')")
    @GetMapping("/lab-documents/{id}")
    public ResponseEntity<LabDocument> get(@PathVariable Long id) {
        final LabDocument document = labDocumentService.get(id).orElseThrow(NotFoundException::new);
        return new ResponseEntity<>(document, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'DOCTOR', 'PATIENT')")
    @GetMapping("/lab-documents/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable Long id) throws MalformedURLException {
        final LabDocument document = labDocumentService.get(id).orElseThrow(NotFoundException::new);
        final Path filePath = Paths.get(document.getFilePath());
        final Resource resource = new UrlResource(filePath.toUri());
        if (!resource.exists() || !resource.isReadable()) {
            throw new NotFoundException();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filePath.getFileName() + "\"")
                .body(resource);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN_SYSTEM', 'DOCTOR')")
    @DeleteMapping("/lab-documents/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        final LabDocument document = labDocumentService.get(id).orElseThrow(NotFoundException::new);
        labDocumentService.delete(document);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
